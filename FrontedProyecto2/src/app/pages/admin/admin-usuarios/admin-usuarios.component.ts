import { Component, inject, OnInit, signal } from '@angular/core';
import { UsuarioService } from '../../../services/usuario/usuario.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UsuarioResponse } from '../../../models/usuario/UsuarioResponse';
import { EnumUsuario } from '../../../models/usuario/EnumUsuario';

@Component({
  selector: 'app-admin-usuarios-component',
  imports: [ReactiveFormsModule],
  templateUrl: './admin-usuarios.component.html'
})
export class AdminUsuariosComponent implements OnInit {
  private usuarioService = inject(UsuarioService);
  private fb = inject(FormBuilder);

  usuarios = signal<UsuarioResponse[]>([]);
  isLoading = signal<boolean>(false);
  errorMessage = signal<string>('');
  successMessage = signal<string>('');

  adminForm: FormGroup = this.fb.group({
    nombreCompleto: ['', [Validators.required, Validators.maxLength(200)]],
    userName: ['', [Validators.required, Validators.maxLength(200)]],
    password: ['', [Validators.required, Validators.maxLength(200)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(200)]],
    telefono: ['', [Validators.required, Validators.pattern('^[0-9]+$'), Validators.maxLength(15), Validators.minLength(8)]],
    direccion: ['', [Validators.required, Validators.maxLength(200)]],
    cui: ['', [Validators.required, Validators.pattern('^[0-9]{13}$'), Validators.maxLength(20), Validators.minLength(13)]],
    fechaNacimiento: ['', [Validators.required]],
    rol: [EnumUsuario.Administrador]
  });

  ngOnInit() {
    this.cargarUsuarios();
  }

  cargarUsuarios() {
    this.usuarioService.getAllUsuarios().subscribe({
      next: (res) => this.usuarios.set(res)
    });
  }

  cambiarEstado(id: number) {
    this.usuarioService.updateUsuarioEstado(id).subscribe({
      next: () => this.cargarUsuarios()
    });
  }

  crearAdmin() {
    if (this.adminForm.invalid) {
      this.adminForm.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.usuarioService.createUsuarioAdmin(this.adminForm.value).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.successMessage.set('Administrador creado con éxito.');
        this.adminForm.reset({ rol: EnumUsuario.Administrador });
        this.cargarUsuarios();
        setTimeout(() => {
          this.successMessage.set('');
        }, 3000);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err.error?.error || 'Error al crear administrador.');
        setTimeout(() => {
          this.errorMessage.set('');
        }, 3000);
      }
    });
  }

  buscarUsuario(event: Event) {
    const termino = (event.target as HTMLInputElement).value.trim();
    
    if (!termino) {
      this.cargarUsuarios();
      return;
    }

    this.usuarioService.getUsuariosByCoincidence(termino).subscribe({
      next: (res) => {
        this.usuarios.set(res);
      },
      error: () => {
        this.usuarios.set([]);
      }
    });
  }
}