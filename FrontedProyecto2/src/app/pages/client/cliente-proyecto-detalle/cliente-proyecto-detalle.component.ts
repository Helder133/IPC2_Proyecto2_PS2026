import { Component, inject, OnInit, signal } from '@angular/core';
import { PropuestaCardComponent } from '../../../components/propuesta-card/propuesta-card.component';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { ProyectoService } from '../../../services/proyecto/proyecto.service';
import { PropuestaService } from '../../../services/propuesta/propuesta.service';
import { ProyectoResponse } from '../../../models/proyecto/ProyectoResponse';
import { PropuestaDetalleResponse } from '../../../models/propuesta/PropuestaDetalleResponse';
import { UsuarioService } from '../../../services/usuario/usuario.service';

@Component({
  selector: 'app-cliente-proyecto-detalle.component',
  imports: [CommonModule, PropuestaCardComponent],
  templateUrl: './cliente-proyecto-detalle.component.html'
})
export class ClienteProyectoDetalleComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private proyectoService = inject(ProyectoService);
  private propuestaService = inject(PropuestaService);
  private usuarioService = inject(UsuarioService);

  proyecto = signal<ProyectoResponse | null>(null);
  propuestas = signal<PropuestaDetalleResponse[]>([]);

  isLoading = signal<boolean>(true);
  errorMessage = signal<string>('');
  successMessage = signal<string>('');

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.iniciarRevisionProyecto(Number(id));
      } else {
        this.volver();
      }
    });
  }

  iniciarRevisionProyecto(proyectoId: number) {
    this.isLoading.set(true);
    this.proyectoService.marcarProyectoEnRevision(proyectoId).subscribe({
      next: () => {
        this.cargarDatos(proyectoId);
      },
      error: (err) => {
        this.cargarDatos(proyectoId);
      }
    });
  }

  cargarDatos(proyectoId: number) {
    this.proyectoService.getUsuarioProyectoById(proyectoId).subscribe({
      next: (proy) => this.proyecto.set(proy),
      error: () => {
        this.errorMessage.set('No se pudo cargar el proyecto.');
        setTimeout(() => this.errorMessage.set(''), 3000);
      }

    });

    this.propuestaService.getAllPropuestaForAProyecto(proyectoId).subscribe({
      next: (props) => {
        this.propuestas.set(props);
        this.isLoading.set(false);
      },
      error: () => {
        this.errorMessage.set('No se pudieron cargar las propuestas.');
        this.isLoading.set(false);
        setTimeout(() => this.errorMessage.set(''), 3000);
      }
    });
  }

  aceptarPropuestaFreelancer(propuestaId: number) {
    if (confirm('¿Estás seguro de aceptar esta propuesta? El monto será bloqueado de tu saldo y se generará un contrato automáticamente.')) {
      this.isLoading.set(true);
      this.errorMessage.set('');

      this.propuestaService.aceptarPropuesta(propuestaId).subscribe({
        next: (res) => {
          this.successMessage.set(res.message || '¡Propuesta aceptada y contrato creado!');
          this.usuarioService.refrescarCarteraGlobal();
          this.cargarDatos(this.proyecto()!.proyectoId);
          setTimeout(() => this.successMessage.set(''), 3000);
        },
        error: (err) => {
          this.isLoading.set(false);
          this.errorMessage.set(err.error?.error || 'Error al aceptar la propuesta. Verifica tu saldo.');
          setTimeout(() => this.errorMessage.set(''), 3000);
        }
      });
    }
  }

  rechazarPropuestaFreelancer(propuestaId: number) {
    if (confirm('¿Deseas rechazar definitivamente esta propuesta?')) {
      this.isLoading.set(true);
      this.propuestaService.rechazarPropuesta(propuestaId).subscribe({
        next: () => {
          this.cargarDatos(this.proyecto()!.proyectoId);
        },
        error: (err) => {
          this.isLoading.set(false);
          this.errorMessage.set(err.error?.error || 'Error al rechazar la propuesta.');
          setTimeout(() => this.errorMessage.set(''), 3000);
        }
      });
    }
  }

  volver() {
    this.router.navigate(['/cliente/proyectos']);
  }
}