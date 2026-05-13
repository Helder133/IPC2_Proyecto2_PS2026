import { Component, EventEmitter, inject, Input, OnInit, Output, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CategoriaResponse } from '../../models/categoria/CategoriaResponse';
import { HabilidadResponse } from '../../models/habilidad/HabilidadResponse';
import { ProyectoResponse } from '../../models/proyecto/ProyectoResponse';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-proyecto-form-component',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './proyecto-form.component.html'
})
export class ProyectoFormComponent implements OnInit {
  private fb = inject(FormBuilder);

  @Input({ required: true }) categorias: CategoriaResponse[] = [];
  @Input({ required: true }) habilidadesDisponibles: HabilidadResponse[] = [];

  @Input() isUpdateMode = false;
  @Input() proyectoAEditar: ProyectoResponse | null = null;
  @Input() isLoading = false;

  @Output() formSubmit = new EventEmitter<{ datosFormulario: any, habilidadesSeleccionadas: number[] }>();
  @Output() onCancel = new EventEmitter<void>();

  proyectoForm!: FormGroup;
  habilidadesSeleccionadas = signal<number[]>([]);
  errorMessage = signal<string>('');

  ngOnInit() {
    this.initForm();
    if (this.isUpdateMode && this.proyectoAEditar) {
      this.cargarDatosEdicion();
    }
  }

  initForm() {
    this.proyectoForm = this.fb.group({
      titulo: ['', [Validators.required, Validators.maxLength(200)]],
      descripcion: ['', [Validators.required, Validators.maxLength(200)]],
      categoriaId: ['', [Validators.required]],
      presupuesto: ['', [Validators.required, Validators.min(1)]],
      fechaLimite: ['', [Validators.required]]
    });
  }

  cargarDatosEdicion() {
    const p = this.proyectoAEditar!;
    this.proyectoForm.patchValue({
      titulo: p.titulo,
      descripcion: p.descripcion,
      categoriaId: p.categoriaId,
      presupuesto: p.presupuesto,
      fechaLimite: p.fechaLimite
    });

    if (p.habilidadResponses) {
      this.habilidadesSeleccionadas.set(p.habilidadResponses.map(h => h.habilidadId));
    }
  }

  toggleHabilidad(id: number) {
    const actuales = this.habilidadesSeleccionadas();
    if (actuales.includes(id)) {
      this.habilidadesSeleccionadas.set(actuales.filter(hId => hId !== id));
    } else {
      this.habilidadesSeleccionadas.set([...actuales, id]);
    }
  }

  enviar() {
    if (this.proyectoForm.invalid) {
      this.proyectoForm.markAllAsTouched();
      if (!this.isUpdateMode && this.habilidadesSeleccionadas().length === 0) {
        this.errorMessage.set('Debes seleccionar al menos una habilidad requerida.');
        return;
      }
      return;
    }
    this.errorMessage.set('');

    this.formSubmit.emit({
      datosFormulario: this.proyectoForm.value,
      habilidadesSeleccionadas: this.habilidadesSeleccionadas()
    });
  }
}