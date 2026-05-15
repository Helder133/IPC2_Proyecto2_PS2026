import { CommonModule } from '@angular/common';
import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { EntregaCardComponent } from '../../../components/entrega-card/entrega-card.component';
import { EntregaFormComponent } from '../../../components/entrega-form/entrega-form.component';
import { ActivatedRoute, Router } from '@angular/router';
import { EntregaService } from '../../../services/entrega/entrega.service';
import { ContratoService } from '../../../services/contrato/contrato.service';
import { ContratoResponse } from '../../../models/contrato/ContratoResponse';
import { EntregaResponse } from '../../../models/entrega/EntregaResponse';
import { EnumEntrega } from '../../../models/entrega/EnumEntrega';
import { EnumContrato } from '../../../models/contrato/EnumContrato';
import { EntregaUpdate } from '../../../models/entrega/EntregaUpdate';
import { EntregaRequest } from '../../../models/entrega/EntregaRequest';

@Component({
  selector: 'app-freelancer-contrato-detalle.component',
  imports: [CommonModule, EntregaCardComponent, EntregaFormComponent],
  templateUrl: './freelancer-contrato-detalle.component.html'
})
export class FreelancerContratoDetalleComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private contratoService = inject(ContratoService);
  private entregaService = inject(EntregaService);
  
  contrato = signal<ContratoResponse | null>(null);
  entregas = signal<EntregaResponse[]>([]);

  entregaPendiente = computed(() => {
    return this.entregas().find(e => e.estado === EnumEntrega.Pendiente) || null;
  });

  isLoading = signal<boolean>(true);
  isSubmitting = signal<boolean>(false);
  errorMessage = signal<string>('');
  successMessage = signal<string>('');

  enumContrato = EnumContrato;

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.cargarDatos(Number(id));
      } else {
        this.volver();
      }
    });
  }

  cargarDatos(contratoId: number) {
    this.isLoading.set(true);

    // 1. Cargamos la información principal del contrato
    this.contratoService.getContratoById(contratoId).subscribe({
      next: (cont) => {
        this.contrato.set(cont);
        // 2. Cargamos el historial de entregas
        this.cargarEntregas(contratoId);
      },
      error: () => this.mostrarError('No se encontró el contrato especificado.')
    });
  }

  cargarEntregas(contratoId: number) {
    this.entregaService.getAllEntregasOfAContract(contratoId).subscribe({
      next: (ents) => {
        // Ordenamos para que la más reciente (y la pendiente si existe) quede arriba
        const ordenadas = ents.sort((a, b) => new Date(b.fecha).getTime() - new Date(a.fecha).getTime());
        this.entregas.set(ordenadas);
        this.isLoading.set(false);
        this.isSubmitting.set(false);
      },
      error: () => this.mostrarError('No se pudo cargar el historial de entregas.')
    });
  }

  procesarFormulario(formValues: { descripcion: string, archivo: string }) {
    this.isSubmitting.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    const pendiente = this.entregaPendiente();

    if (pendiente) {
      // Flujo de Actualización
      const req: EntregaUpdate = {
        entregaId: pendiente.entregaId,
        descripcion: formValues.descripcion,
        archivo: formValues.archivo
      };

      this.entregaService.updateEntrega(req).subscribe({
        next: () => {
          this.successMessage.set('Entrega actualizada exitosamente.');
          this.cargarEntregas(this.contrato()!.contratoId); // Recargamos para actualizar la vista
        },
        error: (err) => this.mostrarSubmitError(err)
      });

    } else {
      const req: EntregaRequest = {
        contratoId: this.contrato()!.contratoId,
        descripcion: formValues.descripcion,
        archivo: formValues.archivo
      };

      this.entregaService.createEntrega(req).subscribe({
        next: () => {
          this.successMessage.set('¡Tu entrega ha sido enviada al cliente para revisión!');
          this.cargarEntregas(this.contrato()!.contratoId); // Recargamos para que aparezca la nueva tarjeta
        },
        error: (err) => this.mostrarSubmitError(err)
      });
    }
  }

  volver() {
    this.router.navigate(['/freelancer/contratos']);
  }

  private mostrarError(msg: string) {
    this.errorMessage.set(msg);
    this.isLoading.set(false);
  }

  private mostrarSubmitError(err: any) {
    this.errorMessage.set(err.error?.error || 'Ocurrió un error al procesar tu entrega.');
    this.isSubmitting.set(false);
  }
}