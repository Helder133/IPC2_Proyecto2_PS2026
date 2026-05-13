import { Component, inject, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth/auth.service';
import { Router, RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';
import { EnumUsuario } from '../../models/usuario/EnumUsuario';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule,CommonModule,RouterLink],
  templateUrl: './login.component.html'
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private router = inject(Router);

  loginForm: FormGroup = this.fb.group({
    username: ['', [Validators.required, Validators.maxLength(200)]],
    password: ['', [Validators.required, Validators.maxLength(200)]]
  });

  errorMessage = signal<string>('');
  isLoading = signal<boolean>(false);

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set('');

    this.authService.login(this.loginForm.value).subscribe({
      next: (res) => {
        this.isLoading.set(false);
        const usuario = res.usuario;
        if (usuario.rol === EnumUsuario.Cliente && !usuario.cliente) {
          this.router.navigate(['/complemento']);
        } 
        else if (usuario.rol === EnumUsuario.Freelancer && !usuario.freelancer) {
          this.router.navigate(['/complemento']);
        }
        else {
          if (usuario.rol === EnumUsuario.Administrador) this.router.navigate(['/admin/dashboard']);
          else if (usuario.rol === EnumUsuario.Cliente) this.router.navigate(['/cliente/dashboard']);
          else this.router.navigate(['/freelancer/dashboard']);
        }
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err.error?.error || 'Ocurrió un error de conexión con el servidor.');
      }
    });
  }
}
