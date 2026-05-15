import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { SolicitudService } from '../../../services/solicitud/solicitud.service';
import { Nueva_h_cResponse } from '../../../models/nueva_h_c/Nueva_h_cResponse';
import { EnumNueva_h_cEstado } from '../../../models/nueva_h_c/EnumNueva_h_cEstado';
import { CommonModule } from '@angular/common';
import { SolicitudCardComponent } from '../../../components/solicitud-card/solicitud-card.component';

@Component({
  selector: 'app-admin-solicitudes.component',
  imports: [CommonModule, SolicitudCardComponent],
  templateUrl: './admin-solicitudes.component.html'
})
export class AdminSolicitudesComponent implements OnInit {
  private solicitudService = inject(SolicitudService);

  solicitudes = signal<Nueva_h_cResponse[]>([]);
  filtroActual = signal<'PENDIENTES' | 'HISTORIAL'>('PENDIENTES');

  isLoading = signal<boolean>(true);
  isProcessing = signal<boolean>(false);
  errorMessage = signal<string>('');
  successMessage = signal<string>('');
  
  enumEstado = EnumNueva_h_cEstado;

  // Filtro en memoria
  solicitudesFiltradas = computed(() => {
    const lista = this.solicitudes();
    if (this.filtroActual() === 'PENDIENTES') {
      return lista.filter(s => s.estado === EnumNueva_h_cEstado.Pendiente);
    } else {
      return lista.filter(s => s.estado !== EnumNueva_h_cEstado.Pendiente);
    }
  });

  ngOnInit() {
    this.cargarSolicitudes();
  }

  cargarSolicitudes() {
    this.isLoading.set(true);
    this.solicitudService.getAllAdmin().subscribe({
      next: (res) => {
        const ordenadas = res.sort((a, b) => new Date(b.fechaCreacion).getTime() - new Date(a.fechaCreacion).getTime());
        this.solicitudes.set(ordenadas);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Error al cargar las solicitudes de la plataforma.');
        this.isLoading.set(false);
      }
    });
  }

  cambiarFiltro(nuevoFiltro: 'PENDIENTES' | 'HISTORIAL') {
    this.filtroActual.set(nuevoFiltro);
    this.errorMessage.set('');
    this.successMessage.set('');
  }
  aprobarSolicitud(id: number) {
    if (!confirm('¿Estás seguro de APROBAR esta solicitud? Se agregará inmediatamente al catálogo global.')) return;

    this.ejecutarAccion(this.solicitudService.aceptarSolicitud(id), 'Solicitud aprobada y agregada al catálogo.');
  }

  rechazarSolicitud(id: number) {
    if (!confirm('¿Estás seguro de RECHAZAR esta solicitud?')) return;

    this.ejecutarAccion(this.solicitudService.rechazarSolicitud(id), 'La solicitud ha sido rechazada.');
  }

  private ejecutarAccion(observableAction: any, mensajeExito: string) {
    this.isProcessing.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    observableAction.subscribe({
      next: () => {
        this.successMessage.set(mensajeExito);
        this.cargarSolicitudes(); // Recarga la lista para actualizar los estados
        this.isProcessing.set(false);
      },
      error: (err: any) => {
        this.errorMessage.set(err.error?.error || 'Ocurrió un error al procesar la solicitud.');
        this.isProcessing.set(false);
      }
    });
  }
}