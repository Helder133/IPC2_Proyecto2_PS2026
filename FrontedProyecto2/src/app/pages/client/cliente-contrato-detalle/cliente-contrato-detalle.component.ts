import { Component, inject, OnInit, signal } from '@angular/core';
import { EntregaCardComponent } from '../../../components/entrega-card/entrega-card.component';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ContratoService } from '../../../services/contrato/contrato.service';
import { EntregaService } from '../../../services/entrega/entrega.service';
import { UsuarioService } from '../../../services/usuario/usuario.service';
import { EntregaResponse } from '../../../models/entrega/EntregaResponse';
import { ContratoResponse } from '../../../models/contrato/ContratoResponse';
import { EnumContrato } from '../../../models/contrato/EnumContrato';
import { ContratoFinalizado } from '../../../models/entrega/ContratoFinalizado';
import { EntregaRechazada } from '../../../models/entrega/EntregaRechazada';
import { ContratoCancelado } from '../../../models/contrato/ContratoCancelado';

@Component({
  selector: 'app-cliente-contrato-detalle.component',
  imports: [CommonModule, ReactiveFormsModule, EntregaCardComponent],
  templateUrl: './cliente-contrato-detalle.component.html'
})
export class ClienteContratoDetalleComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private fb = inject(FormBuilder);
  private contratoService = inject(ContratoService);
  private entregaService = inject(EntregaService);
  private usuarioService = inject(UsuarioService);

  contrato = signal<ContratoResponse | null>(null);
  entregas = signal<EntregaResponse[]>([]);

  accionActual = signal<'ninguna' | 'aprobar' | 'rechazar' | 'cancelar'>('ninguna');
  entregaSeleccionada = signal<number | null>(null);

  isLoading = signal<boolean>(true);
  isSubmitting = signal<boolean>(false);
  errorMessage = signal<string>('');
  successMessage = signal<string>('');

  // Formularios para cada acción
  aprobarForm!: FormGroup;
  rechazarForm!: FormGroup;
  cancelarForm!: FormGroup;

  enumContrato = EnumContrato;

  ngOnInit() {
    this.initForms();
    this.route.queryParams.subscribe(params => {
      const id = params['id'];
      if (id) this.cargarDatos(Number(id));
      else this.volver();
    });
  }

  initForms() {
    this.aprobarForm = this.fb.group({
      calificacion: ['', [Validators.required, Validators.min(1), Validators.max(5)]],
      comentario: ['', [Validators.required, Validators.maxLength(200)]]
    });

    this.rechazarForm = this.fb.group({
      motivo_rechazo: ['', [Validators.required, Validators.maxLength(200)]]
    });

    this.cancelarForm = this.fb.group({
      motivoCancelacion: ['', [Validators.required, Validators.maxLength(200)]]
    });
  }

  cargarDatos(contratoId: number) {
    this.isLoading.set(true);
    this.contratoService.getContratoById(contratoId).subscribe({
      next: (cont) => {
        this.contrato.set(cont);
        this.cargarEntregas(contratoId);
      },
      error: () => this.mostrarError('No se encontró el contrato.')
    });
  }

  cargarEntregas(contratoId: number) {
    this.entregaService.getAllEntregasOfAContract(contratoId).subscribe({
      next: (ents) => {
        this.entregas.set(ents.sort((a, b) => new Date(b.fecha).getTime() - new Date(a.fecha).getTime()));
        this.isLoading.set(false);
        this.isSubmitting.set(false);
        this.accionActual.set('ninguna'); 
      },
      error: () => this.mostrarError('Error al cargar entregas.')
    });
  }

  iniciarAprobacion(entregaId: number) {
    this.entregaSeleccionada.set(entregaId);
    this.aprobarForm.reset();
    this.accionActual.set('aprobar');
  }

  iniciarRechazo(entregaId: number) {
    this.entregaSeleccionada.set(entregaId);
    this.rechazarForm.reset();
    this.accionActual.set('rechazar');
  }

  iniciarCancelacion() {
    this.cancelarForm.reset();
    this.accionActual.set('cancelar');
  }

  cancelarAccion() {
    this.accionActual.set('ninguna');
    this.entregaSeleccionada.set(null);
  }

  confirmarAprobacion() {
    if (this.aprobarForm.invalid) { this.aprobarForm.markAllAsTouched(); return; }
    if (!confirm('Al aprobar la entrega, el contrato finalizará y se liberará el pago al freelancer. ¿Continuar?')) return;

    this.isSubmitting.set(true);
    const req: ContratoFinalizado = {
      contratoId: this.contrato()!.contratoId,
      comentario: this.aprobarForm.value.comentario,
      calificacion: this.aprobarForm.value.calificacion
    };

    this.entregaService.aprobarEntrega(this.entregaSeleccionada()!, req).subscribe({
      next: () => {
        this.successMessage.set('¡Entrega aprobada y contrato finalizado!');
        this.usuarioService.refrescarCarteraGlobal(); // Actualizamos saldo (se liberó el pago)
        this.cargarDatos(this.contrato()!.contratoId);
      },
      error: (err) => this.mostrarSubmitError(err)
    });
  }

  confirmarRechazo() {
    if (this.rechazarForm.invalid) { this.rechazarForm.markAllAsTouched(); return; }

    this.isSubmitting.set(true);
    const req: EntregaRechazada = {
      entregaId: this.entregaSeleccionada()!,
      motivo_rechazo: this.rechazarForm.value.motivo_rechazo
    };

    this.entregaService.rechazarEntrega(req).subscribe({
      next: () => {
        this.successMessage.set('La entrega ha sido rechazada. El freelancer ha sido notificado.');
        this.cargarDatos(this.contrato()!.contratoId);
      },
      error: (err) => this.mostrarSubmitError(err)
    });
  }

  confirmarCancelacion() {
    if (this.cancelarForm.invalid) { this.cancelarForm.markAllAsTouched(); return; }
    if (!confirm('¿Estás SEGURO de cancelar este contrato? El saldo bloqueado será devuelto a tu cuenta.')) return;

    this.isSubmitting.set(true);
    const req: ContratoCancelado = {
      contratoId: this.contrato()!.contratoId,
      motivoCancelacion: this.cancelarForm.value.motivoCancelacion
    };

    this.contratoService.cancelarContrato(req).subscribe({
      next: () => {
        this.successMessage.set('El contrato ha sido cancelado exitosamente.');
        this.usuarioService.refrescarCarteraGlobal();
        this.cargarDatos(this.contrato()!.contratoId);
      },
      error: (err) => this.mostrarSubmitError(err)
    });
  }

  volver() {
    this.router.navigate(['/cliente/contratos']);
  }

  private mostrarError(msg: string) { this.errorMessage.set(msg); this.isLoading.set(false); }
  private mostrarSubmitError(err: any) { this.errorMessage.set(err.error?.error || 'Error al procesar la solicitud.'); this.isSubmitting.set(false); }
}