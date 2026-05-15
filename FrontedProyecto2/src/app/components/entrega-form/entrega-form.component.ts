import { Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { EntregaResponse } from '../../models/entrega/EntregaResponse';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-entrega-form-component',
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './entrega-form.component.html'
})
export class EntregaFormComponent implements OnInit, OnChanges {
  private fb = inject(FormBuilder);

  @Input() entregaPendiente: EntregaResponse | null = null;
  @Input() isSubmitting = false;

  @Output() formSubmit = new EventEmitter<{ descripcion: string, archivo: string }>();

  entregaForm!: FormGroup;

  ngOnInit() {
    this.initForm();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (this.entregaForm && changes['entregaPendiente']) {
      this.cargarDatos();
    }
  }

  initForm() {
    this.entregaForm = this.fb.group({
      descripcion: ['', [Validators.required, Validators.maxLength(200)]],
      archivo: ['', [Validators.required, Validators.maxLength(200)]]
    });
    this.cargarDatos();
  }

  cargarDatos() {
    if (this.entregaPendiente) {
      this.entregaForm.patchValue({
        descripcion: this.entregaPendiente.descripcion,
        archivo: this.entregaPendiente.archivo
      });
    } else {
      this.entregaForm.reset();
    }
  }

  enviar() {
    if (this.entregaForm.invalid) {
      this.entregaForm.markAllAsTouched();
      return;
    }
    // Emitimos los valores limpios al padre
    this.formSubmit.emit(this.entregaForm.value);
  }
}