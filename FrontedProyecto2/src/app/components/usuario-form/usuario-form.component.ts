import { Component, EventEmitter, inject, Input, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UsuarioRequest } from '../../models/usuario/UsuarioRequest';
import { EnumUsuario } from '../../models/usuario/EnumUsuario';

@Component({
  selector: 'app-usuario-form-component',
  imports: [ReactiveFormsModule],
  templateUrl: './usuario-form.component.html'
})
export class UsuarioFormComponent {
  private fb = inject(FormBuilder);

  @Input() isAdminMode: boolean = false;
  @Input() isLoading: boolean = false;
  @Output() formSubmit = new EventEmitter<UsuarioRequest>();

  usuarioForm: FormGroup = this.fb.group({
    nombreCompleto: ['', [Validators.required, Validators.maxLength(200)]],
    userName: ['', [Validators.required, Validators.maxLength(200)]],
    password: ['', [Validators.required, Validators.maxLength(200)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(200)]],
    telefono: ['', [Validators.required, Validators.pattern('^[0-9]+$'), Validators.maxLength(15), Validators.minLength(8)]],
    direccion: ['', [Validators.required, Validators.maxLength(200)]],
    cui: ['', [Validators.required, Validators.pattern('^[0-9]{13}$'), Validators.maxLength(20), Validators.minLength(13)]],
    fechaNacimiento: ['', [Validators.required]],
    rol: ['', [Validators.required]]
  });

  EnumUsuario = EnumUsuario;

  submitForm() {
    if (this.usuarioForm.invalid) {
      this.usuarioForm.markAllAsTouched();
      return;
    }
    this.formSubmit.emit(this.usuarioForm.value as UsuarioRequest);
  }
}
