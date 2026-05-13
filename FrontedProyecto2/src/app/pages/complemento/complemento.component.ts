import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { AuthService } from '../../services/auth/auth.service';
import { UsuarioService } from '../../services/usuario/usuario.service';
import { Router } from '@angular/router';
import { ClienteRequest } from '../../models/usuario/client/ClienteRequest';
import { FreelancerRequest } from '../../models/usuario/freelancer/FreelancerRequest';
import { ComplementoClienteComponent } from "../../components/complemento-cliente/complemento-cliente.component";
import { ComplementoFreelancerComponent } from "../../components/complemento-freelancer/complemento-freelancer.component";
import { EnumUsuario } from '../../models/usuario/EnumUsuario';

@Component({
  selector: 'app-complemento.component',
  imports: [ComplementoClienteComponent, ComplementoFreelancerComponent],
  templateUrl: './complemento.component.html'
})
export class ComplementoComponent implements OnInit {
  private authService = inject(AuthService);
  private usuarioService = inject(UsuarioService);
  private router = inject(Router);

  rolUsuario = signal<EnumUsuario>(null!);
  isLoading = signal<boolean>(false);
  errorMessage = signal<string>('');
  enumUsuario = EnumUsuario;

  ngOnInit() {
    const user = this.authService.currentUser();
    if (user) {
      this.rolUsuario.set(user.rol);
    } else {
      this.router.navigate(['/login']);
    }
  }

  onClienteSubmit(datos: ClienteRequest) {
    this.procesarEnvio(this.usuarioService.insertComplementoCliente(datos), '/cliente/dashboard');
  }

  onFreelancerSubmit(datos: FreelancerRequest) {
    this.procesarEnvio(this.usuarioService.insertComplementoFreelancer(datos), '/freelancer/dashboard');
  }

  private procesarEnvio(peticion: any, rutaDestino: string) {
    this.isLoading.set(true);
    this.errorMessage.set('');

    peticion.subscribe({
      next: () => {
        this.isLoading.set(false);
        this.router.navigate([rutaDestino]);
      },
      error: (err: any) => {
        this.isLoading.set(false);
        this.errorMessage.set(err.error?.error || 'Error al guardar los datos.');
      }
    });
  }
}