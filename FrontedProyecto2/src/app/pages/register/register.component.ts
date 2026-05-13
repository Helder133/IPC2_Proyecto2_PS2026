import { Component, inject, signal } from '@angular/core';
import { UsuarioFormComponent } from '../../components/usuario-form/usuario-form.component';
import { Router, RouterLink } from '@angular/router';
import { UsuarioService } from '../../services/usuario/usuario.service';
import { UsuarioRequest } from '../../models/usuario/UsuarioRequest';

@Component({
  selector: 'app-register.component',
  imports: [UsuarioFormComponent,RouterLink],
  templateUrl: './register.component.html'
})
export class RegisterComponent {
  private usuarioService = inject(UsuarioService);
  private router = inject(Router);

  isLoading = signal<boolean>(false);
  errorMessage = signal<string>('');
  successMessage = signal<string>('');

  onRegister(usuarioData: UsuarioRequest) {
    this.isLoading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.usuarioService.createUsuarioClienteFreelancer(usuarioData).subscribe({
      next: (res) => {
        this.isLoading.set(false);
        this.successMessage.set('¡Registro exitoso! Redirigiendo al login...');
        
        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err.error?.error || 'Error al registrar el usuario.');
      }
    });
  }
}
