import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { UsuarioService } from '../../../services/usuario/usuario.service';
import { HabilidadService } from '../../../services/habilidad/habilidad.service';
import { AuthService } from '../../../services/auth/auth.service';
import { HabilidadResponse } from '../../../models/habilidad/HabilidadResponse';
import { FreelancerHabilidadRequest } from '../../../models/usuario/freelancer/FreelancerHabilidadRequest';

@Component({
  selector: 'app-freelancer-habilidades.component',
  imports: [],
  templateUrl: './freelancer-habilidades.component.html'
})
export class FreelancerHabilidadesComponent implements OnInit {
  private usuarioService = inject(UsuarioService);
  private habilidadService = inject(HabilidadService);
  private authService = inject(AuthService);

  misHabilidades = signal<HabilidadResponse[]>([]);
  habilidadesDisponibles = signal<HabilidadResponse[]>([]);
  
  isLoading = signal<boolean>(false);

  ngOnInit() {
    this.cargarDatos();
  }

  cargarDatos() {
    const user = this.authService.currentUser();
    if (user?.freelancer?.habilidadResponses) {
      this.misHabilidades.set(user.freelancer.habilidadResponses);
    }

    this.cargarDisponiblesDesdeBackend();
  }

  cargarDisponiblesDesdeBackend() {
    this.habilidadService.getHabilidadesDisponiblesFreelancer().subscribe({
      next: (res) => this.habilidadesDisponibles.set(res),
      error: (err) => console.error('Error al cargar disponibles', err)
    });
  }

  agregarHabilidad(id: number) {
    this.isLoading.set(true);
    const req: FreelancerHabilidadRequest = { habilidadId: id };

    this.usuarioService.agregarHabilidad(req).subscribe({
      next: () => {
        this.refrescarUsuario();
      },
      error: () => this.isLoading.set(false)
    });
  }

  eliminarHabilidad(id: number) {
    this.isLoading.set(true);
    this.usuarioService.deleteFreelancerHabilidad(id).subscribe({
      next: () => {
        this.refrescarUsuario();
      },
      error: () => this.isLoading.set(false)
    });
  }

  private refrescarUsuario() {
    const user = this.authService.currentUser();
    if (!user) return;

    this.usuarioService.getUsuarioById(user.usuarioId).subscribe(userActualizado => {
      this.authService.currentUser.set(userActualizado);
      localStorage.setItem('usuario', JSON.stringify(userActualizado));
      
      this.misHabilidades.set(userActualizado.freelancer?.habilidadResponses || []);
      
      this.cargarDisponiblesDesdeBackend();
      
      this.isLoading.set(false);
    });
  }
}