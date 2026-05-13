import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { ClienteRequest } from '../../models/usuario/client/ClienteRequest';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

@Component({
  selector: 'app-complemento-cliente-component',
  imports: [ReactiveFormsModule],
  templateUrl: './complemento-cliente.component.html'
})
export class ComplementoClienteComponent {
  private fb = inject(FormBuilder);

  @Input() isLoading: boolean = false;
  @Output() formSubmit = new EventEmitter<ClienteRequest>();

  clienteForm: FormGroup = this.fb.group({
    descripcion: ['', [Validators.required, Validators.maxLength(200)]],
    sector: ['', [Validators.required, Validators.maxLength(100)]],
    sitioWeb: ['', [Validators.maxLength(100)]]
  });

  submitForm() {
    if (this.clienteForm.invalid) {
      this.clienteForm.markAllAsTouched();
      return;
    }
    this.formSubmit.emit(this.clienteForm.value as ClienteRequest);
  }
}
