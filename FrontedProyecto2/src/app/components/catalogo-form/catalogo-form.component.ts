import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CatalogoEnum } from '../../models/catalogo/CatalogoEnum';

@Component({
  selector: 'app-catalogo-form-component',
  imports: [ReactiveFormsModule],
  templateUrl: './catalogo-form.component.html'
})
export class CatalogoFormComponent implements OnInit {
  private fb = inject(FormBuilder);

  @Input() isLoading: boolean = false;
  @Input() isUpdateMode: boolean = false;
  @Input() tipoForzado: CatalogoEnum | '' = '';
  @Input() datosIniciales: any = null;

  @Output() formSubmit = new EventEmitter<{ tipo: CatalogoEnum, datos: any }>();
  @Output() onCancel = new EventEmitter<void>();

  CatalogoEnum = CatalogoEnum;

  catalogoForm: FormGroup = this.fb.group({
    id: [null],
    tipo: ['', Validators.required],
    nombre: ['', [Validators.required, Validators.maxLength(100)]],
    descripcion: ['', [Validators.required, Validators.maxLength(200)]]
  });

  ngOnInit() {
    if (this.tipoForzado) {
      this.catalogoForm.patchValue({ tipo: this.tipoForzado });
    }
    if (this.isUpdateMode && this.datosIniciales) {
      this.catalogoForm.patchValue(this.datosIniciales);
    }
  }

  submitForm() {
    if (this.catalogoForm.invalid) {
      this.catalogoForm.markAllAsTouched();
      return;
    }

    const { tipo, ...datos } = this.catalogoForm.value;
    this.formSubmit.emit({ tipo, datos });
    if (!this.isUpdateMode) this.catalogoForm.reset({ tipo: this.tipoForzado });
  }
}
