import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { ContratoCardComponent } from '../../../components/contrato-card/contrato-card.component';
import { CommonModule } from '@angular/common';
import { ContratoService } from '../../../services/contrato/contrato.service';
import { Router } from '@angular/router';
import { ContratoResponse } from '../../../models/contrato/ContratoResponse';
import { EnumContrato } from '../../../models/contrato/EnumContrato';

@Component({
  selector: 'app-cliente-contratos-component',
  imports: [CommonModule, ContratoCardComponent],
  templateUrl: './cliente-contratos.component.html'
})
export class ClienteContratosComponent implements OnInit {
  private contratoService = inject(ContratoService);
  private router = inject(Router);

  contratos = signal<ContratoResponse[]>([]);
  filtroActual = signal<'ACTIVOS' | 'HISTORIAL'>('ACTIVOS');
  isLoading = signal<boolean>(false);

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
    this.contratoService.getAllContractsFromACliente().subscribe({
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
    this.router.navigate(['/cliente/contrato-detalle'], { queryParams: { id: contratoId } });
  }
}