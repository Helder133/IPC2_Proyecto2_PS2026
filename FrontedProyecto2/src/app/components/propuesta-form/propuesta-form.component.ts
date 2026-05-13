import { Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { PropuestaResponse } from '../../models/propuesta/PropuestaResponse';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-propuesta-form-component',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './propuesta-form.component.html'
})
export class PropuestaFormComponent implements OnInit, OnChanges {
  private fb = inject(FormBuilder);

  @Input({ required: true }) presupuestoMaximo!: number;
  @Input() propuestaActual: PropuestaResponse | null = null;
  @Input() isLoading = false;

  @Output() formSubmit = new EventEmitter<any>();

  propuestaForm!: FormGroup;

  ngOnInit() {
    this.initForm();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (this.propuestaForm && changes['propuestaActual']) {
      this.cargarDatos();
    }
  }

  initForm() {
    this.propuestaForm = this.fb.group({
      monto: ['', [Validators.required, Validators.min(1), Validators.max(this.presupuestoMaximo)]],
      tiempoEntrega: ['', [Validators.required, Validators.min(1)]],
      descripcion: ['', Validators.required]
    });
    this.cargarDatos();
  }

  cargarDatos() {
    if (this.propuestaActual) {
      // Si ya hay propuesta, autollenamos
      this.propuestaForm.patchValue({
        monto: this.propuestaActual.monto,
        tiempoEntrega: this.propuestaActual.tiempoEntrega,
        descripcion: this.propuestaActual.descripcion
      });
    } else {
      // Si no, limpiamos
      this.propuestaForm.reset();
    }
  }

  enviar() {
    if (this.propuestaForm.invalid) {
      this.propuestaForm.markAllAsTouched();
      return;
    }
    // Emitimos los valores al componente padre
    this.formSubmit.emit(this.propuestaForm.value);
  }
}