import { CommonModule } from '@angular/common';
import { Component, EventEmitter, inject, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Nueva_h_cResponse } from '../../models/nueva_h_c/Nueva_h_cResponse';
import { EnumNueva_h_cTipo } from '../../models/nueva_h_c/EnumNueva_h_cTipo';

@Component({
  selector: 'app-solicitud-form-component',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './solicitud-form.component.html'
})
export class SolicitudFormComponent implements OnInit, OnChanges {
  private fb = inject(FormBuilder);

  @Input() solicitudEditando: Nueva_h_cResponse | null = null;
  @Input() isSubmitting = false;
  @Input() tipoCatalogo!: EnumNueva_h_cTipo;

  @Output() formSubmit = new EventEmitter<{ nombre: string, descripcion: string }>();

  solicitudForm!: FormGroup;
  enumTipo = EnumNueva_h_cTipo;

  ngOnInit() {
    this.initForm();
  }

  ngOnChanges(changes: SimpleChanges) {
    if (this.solicitudForm && changes['solicitudEditando']) {
      this.cargarDatos();
    }
  }

  initForm() {
    this.solicitudForm = this.fb.group({
      nombre: ['', [Validators.required, Validators.maxLength(200)]],
      descripcion: ['', [Validators.required, Validators.maxLength(200)]]
    });
    this.cargarDatos();
  }

  cargarDatos() {
    if (this.solicitudEditando) {
      this.solicitudForm.patchValue({
        nombre: this.solicitudEditando.nombre,
        descripcion: this.solicitudEditando.descripcion
      });
    } else {
      this.solicitudForm.reset();
    }
  }

  enviar() {
    if (this.solicitudForm.invalid) {
      this.solicitudForm.markAllAsTouched();
      return;
    }
    // Mandamos los datos limpios al padre
    this.formSubmit.emit(this.solicitudForm.value);
  }
}
