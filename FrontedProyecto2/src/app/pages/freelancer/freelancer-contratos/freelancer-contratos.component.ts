import { CommonModule } from '@angular/common';
import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { ContratoCardComponent } from '../../../components/contrato-card/contrato-card.component';
import { ContratoService } from '../../../services/contrato/contrato.service';
import { Router } from '@angular/router';
import { ContratoResponse } from '../../../models/contrato/ContratoResponse';
import { EnumContrato } from '../../../models/contrato/EnumContrato';

@Component({
  selector: 'app-freelancer-contratos.component',
  imports: [CommonModule, ContratoCardComponent],
  templateUrl: './freelancer-contratos.component.html'
})
export class FreelancerContratosComponent implements OnInit {
  private contratoService = inject(ContratoService);
  private router = inject(Router);

  contratos = signal<ContratoResponse[]>([]);
  filtroActual = signal<'ACTIVOS' | 'HISTORIAL'>('ACTIVOS');
  isLoading = signal<boolean>(false);

  // Filtramos en memoria ya que los traemos todos en una sola petición
  contratosFiltrados = computed(() => {
    const lista = this.contratos();
    if (this.filtroActual() === 'ACTIVOS') {
      return lista.filter(c => c.estado === EnumContrato.Activo);
    } else {
      return lista.filter(c => c.estado !== EnumContrato.Activo);
    }
  });

  ngOnInit() {
    this.cargarContratos();
  }

  cargarContratos() {
    this.isLoading.set(true);
    this.contratoService.getAllContractsFromAFreelancer().subscribe({
      next: (res) => {
        this.contratos.set(res);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }

  cambiarFiltro(nuevoFiltro: 'ACTIVOS' | 'HISTORIAL') {
    this.filtroActual.set(nuevoFiltro);
  }

  abrirEspacioTrabajo(contratoId: number) {
    // Redirigiremos a la vista detallada del contrato (donde se suben las entregas)
    this.router.navigate(['/freelancer/contrato-detalle'], { queryParams: { id: contratoId } });
  }
}