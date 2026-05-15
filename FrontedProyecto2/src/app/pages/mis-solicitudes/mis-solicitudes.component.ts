import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { CatalogoFormComponent } from '../../components/catalogo-form/catalogo-form.component';
import { SolicitudCardComponent } from '../../components/solicitud-card/solicitud-card.component';
import { AuthService } from '../../services/auth/auth.service';
import { SolicitudService } from '../../services/solicitud/solicitud.service';
import { Nueva_h_cResponse } from '../../models/nueva_h_c/Nueva_h_cResponse';
import { EnumNueva_h_cTipo } from '../../models/nueva_h_c/EnumNueva_h_cTipo';
import { EnumNueva_h_cEstado } from '../../models/nueva_h_c/EnumNueva_h_cEstado';
import { EnumUsuario } from '../../models/usuario/EnumUsuario';
import { Nueva_h_cUpdate } from '../../models/nueva_h_c/Nueva_h_cUpdate';
import { Nueva_h_cRequest } from '../../models/nueva_h_c/Nueva_h_cRequest';
import { SolicitudFormComponent } from "../../components/solicitud-form/solicitud-form.component";

@Component({
  selector: 'app-mis-solicitudes.component',
  imports: [CommonModule, SolicitudCardComponent, SolicitudFormComponent],
  templateUrl: './mis-solicitudes.component.html'
})
export class MisSolicitudesComponent implements OnInit {
  private authService = inject(AuthService);
  private solicitudService = inject(SolicitudService);

  solicitudes = signal<Nueva_h_cResponse[]>([]);
  solicitudEditando = signal<Nueva_h_cResponse | null>(null);

  isLoading = signal<boolean>(true);
  isSubmitting = signal<boolean>(false);
  errorMessage = signal<string>('');
  successMessage = signal<string>('');

  tipoCatalogo: EnumNueva_h_cTipo;
  nombreCatalogoUI: string;
  enumEstado = EnumNueva_h_cEstado;

  constructor() {
    const rol = this.authService.currentUser()?.rol;
    if (rol === EnumUsuario.Cliente) {
      this.tipoCatalogo = EnumNueva_h_cTipo.Categoria;
      this.nombreCatalogoUI = 'Categoría';
    } else {
      this.tipoCatalogo = EnumNueva_h_cTipo.Habilidad;
      this.nombreCatalogoUI = 'Habilidad';
    }
  }

  ngOnInit() {
    this.cargarSolicitudes();
  }

  cargarSolicitudes() {
    this.isLoading.set(true);
    this.solicitudService.getMisSolicitudes().subscribe({
      next: (res) => {
        this.solicitudes.set(res.sort((a, b) => new Date(b.fechaCreacion).getTime() - new Date(a.fechaCreacion).getTime()));
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('Error al cargar tu historial de solicitudes.');
        this.isLoading.set(false);
      }
    });
  }

  iniciarEdicion(solicitud: Nueva_h_cResponse) {
    if (solicitud.estado === EnumNueva_h_cEstado.Pendiente) {
      this.solicitudEditando.set(solicitud);
      this.errorMessage.set('');
      this.successMessage.set('');
    }
  }

  cancelarEdicion() {
    this.solicitudEditando.set(null);
  }

  procesarFormulario(formData: { nombre: string, descripcion: string }) {
    this.isSubmitting.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    const editando = this.solicitudEditando();

    if (editando) {
      // ACTUALIZAR
      const req: Nueva_h_cUpdate = {
        solicitudId: editando.solicitudId,
        nombre: formData.nombre,
        descripcion: formData.descripcion
      };

      this.solicitudService.updateSolicitud(req).subscribe({
        next: (res) => {
          this.successMessage.set(res.message || 'Solicitud actualizada.');
          this.finalizarProceso();
        },
        error: (err) => this.mostrarErrorSubmit(err)
      });
    } else {
      // CREAR
      const req: Nueva_h_cRequest = {
        usuarioId: 0,
        nombre: formData.nombre,
        descripcion: formData.descripcion,
        tipo: this.tipoCatalogo
      };

      this.solicitudService.createSolicitud(req).subscribe({
        next: () => {
          this.successMessage.set('Solicitud enviada a los administradores.');
          this.finalizarProceso();
        },
        error: (err) => this.mostrarErrorSubmit(err)
      });
    }
  }

  private finalizarProceso() {
    this.isSubmitting.set(false);
    this.cancelarEdicion();
    this.cargarSolicitudes();
  }

  private mostrarErrorSubmit(err: any) {
    this.errorMessage.set(err.error?.error || 'Error al procesar la solicitud.');
    this.isSubmitting.set(false);
  }
}