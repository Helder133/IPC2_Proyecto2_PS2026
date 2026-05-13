import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { UsuarioService } from '../../services/usuario/usuario.service';
import { EnumUsuario } from '../../models/usuario/EnumUsuario';
import { UsuarioUpdate } from '../../models/usuario/UsuarioUpdate';

@Component({
  selector: 'app-mi-perfil-component',
  imports: [ReactiveFormsModule],
  templateUrl: './mi-perfil.component.html'
})
export class MiPerfilComponent implements OnInit {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private usuarioService = inject(UsuarioService);

  enumUsuario = EnumUsuario;
  usuarioActual = this.authService.currentUser; 

  perfilForm!: FormGroup;
  isLoading = signal<boolean>(false);
  successMessage = signal<string>('');
  errorMessage = signal<string>('');

  ngOnInit() {
    this.construirFormulario();
    this.cargarDatos();
  }

  construirFormulario() {
    this.perfilForm = this.fb.group({
      nombreCompleto: ['', Validators.required],
      userName: ['', Validators.required],
      password: [''],
      email: ['', [Validators.required, Validators.email]],
      telefono: ['', Validators.required],
      direccion: ['', Validators.required],
      cui: ['', Validators.required],
      fechaNacimiento: ['', Validators.required],
    });

    const rol = this.usuarioActual()?.rol;

    if (rol === this.enumUsuario.Cliente) {
      this.perfilForm.addControl('clienteUpdate', this.fb.group({
        descripcion: ['', Validators.required],
        sector: ['', Validators.required],
        sitioWeb: ['']
      }));
    }

    else if (rol === this.enumUsuario.Freelancer) {
      this.perfilForm.addControl('freelancerUpdate', this.fb.group({
        descripcion: ['', Validators.required],
        experiencia: ['', Validators.required],
        tarifaHora: ['', [Validators.required, Validators.min(1)]]
      }));
    }
  }

  cargarDatos() {
    const user = this.usuarioActual();
    if (!user) return;

    this.perfilForm.patchValue({
      nombreCompleto: user.nombreCompleto,
      userName: user.userName,
      email: user.email,
      telefono: user.telefono,
      direccion: user.direccion,
      cui: user.cui,
      fechaNacimiento: user.fechaNacimiento,
    });

    if (user.rol === this.enumUsuario.Cliente && user.cliente) {
      this.perfilForm.get('clienteUpdate')?.patchValue({
        descripcion: user.cliente.descripcion,
        sector: user.cliente.sector,
        sitioWeb: user.cliente.sitioWeb
      });
    } else if (user.rol === this.enumUsuario.Freelancer && user.freelancer) {
      this.perfilForm.get('freelancerUpdate')?.patchValue({
        descripcion: user.freelancer.descripcion,
        experiencia: user.freelancer.experiencia,
        tarifaHora: user.freelancer.tarifaHora
      });
    }
  }

  actualizarPerfil() {
    if (this.perfilForm.invalid) {
      this.perfilForm.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    this.successMessage.set('');
    this.errorMessage.set('');

    const formValue = this.perfilForm.value;
    const user = this.usuarioActual()!;

    const updateReq: UsuarioUpdate = {
      usuarioId: user.usuarioId,
      nombreCompleto: formValue.nombreCompleto,
      userName: formValue.userName,
      password: formValue.password || '',
      email: formValue.email,
      telefono: formValue.telefono,
      direccion: formValue.direccion,
      cui: formValue.cui,
      fechaNacimiento: formValue.fechaNacimiento,
    };

    if (user.rol === this.enumUsuario.Cliente) {
      updateReq.clienteUpdate = { usuarioId: user.usuarioId, ...formValue.clienteUpdate };
    } else if (user.rol === this.enumUsuario.Freelancer) {
      updateReq.freelancerUpdate = { usuarioId: user.usuarioId, ...formValue.freelancerUpdate };
    }

    this.usuarioService.updateUsuarioActualizar(updateReq).subscribe({
      next: () => {
        this.usuarioService.getUsuarioById(user.usuarioId).subscribe(userRefrescado => {
          this.authService.currentUser.set(userRefrescado);
          localStorage.setItem('usuario', JSON.stringify(userRefrescado));
        });

        this.isLoading.set(false);
        this.successMessage.set('Tus datos han sido actualizados exitosamente.');
        this.perfilForm.get('password')?.reset();
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err.error?.error || 'Error al actualizar el perfil.');
      }
    });
  }
}