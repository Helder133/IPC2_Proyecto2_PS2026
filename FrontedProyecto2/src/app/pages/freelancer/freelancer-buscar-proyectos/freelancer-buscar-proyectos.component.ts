import { Component, inject, OnInit, signal } from '@angular/core';
import { ProyectoCardComponent } from '../../../components/proyecto-card/proyecto-card.component';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { ProyectoService } from '../../../services/proyecto/proyecto.service';
import { PropuestaService } from '../../../services/propuesta/propuesta.service';
import { AuthService } from '../../../services/auth/auth.service';
import { ProyectoResponse } from '../../../models/proyecto/ProyectoResponse';
import { PropuestaRequest } from '../../../models/propuesta/PropuestaRequest';
import { EnumUsuario } from '../../../models/usuario/EnumUsuario';
import { PropuestaUpdate } from '../../../models/propuesta/PropuestaUpdate';
import { PropuestaResponse } from '../../../models/propuesta/PropuestaResponse';
import { EnumPropuesta } from '../../../models/propuesta/EnumPropuesta';
import { PropuestaFormComponent } from "../../../components/propuesta-form/propuesta-form.component";
import { HabilidadService } from '../../../services/habilidad/habilidad.service';
import { CategoriaService } from '../../../services/categoria/categoria.service';
import { HabilidadResponse } from '../../../models/habilidad/HabilidadResponse';
import { CategoriaResponse } from '../../../models/categoria/CategoriaResponse';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-freelancer-buscar-proyectos-component',
  imports: [CommonModule, ReactiveFormsModule, ProyectoCardComponent, PropuestaFormComponent],
  templateUrl: './freelancer-buscar-proyectos.component.html'
})
export class FreelancerBuscarProyectosComponent implements OnInit {
  private proyectoService = inject(ProyectoService);
  private propuestaService = inject(PropuestaService);
  private authService = inject(AuthService);
  private categoriaService = inject(CategoriaService);
  private habilidadService = inject(HabilidadService);
  private route = inject(ActivatedRoute);
  
  categoriasFiltro = signal<CategoriaResponse[]>([]);
  habilidadesFiltro = signal<HabilidadResponse[]>([]);

  proyectos = signal<ProyectoResponse[]>([]);
  vistaActual = signal<'lista' | 'gestion'>('lista');

  proyectoSeleccionado = signal<ProyectoResponse | null>(null);
  miPropuestaActual = signal<PropuestaResponse | null>(null);

  enumUsuario = EnumUsuario;
  enumPropuesta = EnumPropuesta;
  isLoading = signal<boolean>(false);
  errorMessage = signal<string>('');
  successMessage = signal<string>('');

  ngOnInit() {
    this.cargarProyectosAbiertos();
    this.categoriaService.getActivadaCategoria().subscribe(res => this.categoriasFiltro.set(res));
    this.habilidadService.getAllHabilidadActivada().subscribe(res => this.habilidadesFiltro.set(res));
    this.route.queryParams.subscribe(params => {
      const id = params['id'];
      if (id) {
        this.abrirProyectoDesdeUrl(Number(id));
      }
    });
  }

  abrirProyectoDesdeUrl(proyectoId: number) {
    this.isLoading.set(true);
    this.proyectoService.getProyectoById(proyectoId).subscribe({
      next: (proyecto) => {
        // Aprovechamos la lógica que ya teníamos
        this.iniciarGestionPropuesta(proyecto);
      },
      error: () => {
        this.isLoading.set(false);
        this.errorMessage.set('No se pudo cargar el detalle del proyecto seleccionado.');
      }
    });
  }

  cargarProyectosAbiertos() {
    this.isLoading.set(true);
    this.proyectoService.getAllProyectosAbiertos().subscribe({
      next: (res) => {
        this.proyectos.set(res);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
    });
  }

  limpiarFiltros() {
    this.cargarProyectosAbiertos();
  }

  filtrarPorCategoria(event: Event) {
    const id = (event.target as HTMLSelectElement).value;
    if (!id) return this.limpiarFiltros();
    
    this.isLoading.set(true);
    this.proyectoService.getProyectosByCategoria(Number(id)).subscribe({
      next: (res) => { this.proyectos.set(res); this.isLoading.set(false); },
      error: () => { this.proyectos.set([]); this.isLoading.set(false); }
    });
  }

  filtrarPorHabilidad(event: Event) {
    const id = (event.target as HTMLSelectElement).value;
    if (!id) return this.limpiarFiltros();
    
    this.isLoading.set(true);
    this.proyectoService.getProyectosByHabilidad(Number(id)).subscribe({
      next: (res) => { this.proyectos.set(res); this.isLoading.set(false); },
      error: () => { this.proyectos.set([]); this.isLoading.set(false); }
    });
  }

  
  filtrarPorPresupuesto(minInput: HTMLInputElement, maxInput: HTMLInputElement) {
    const min = Number(minInput.value);
    const max = Number(maxInput.value);

    if (min < 0 || max <= 0 || min > max) {
      this.errorMessage.set('Rango de presupuesto inválido.');
      return;
    }

    this.errorMessage.set('');
    this.isLoading.set(true);
    this.proyectoService.getProyectosByPresupuesto(min, max).subscribe({
      next: (res) => { this.proyectos.set(res); this.isLoading.set(false); },
      error: () => { this.proyectos.set([]); this.isLoading.set(false); }
    });
  }

  iniciarGestionPropuesta(proyecto: ProyectoResponse) {
    this.errorMessage.set('');
    this.successMessage.set('');

    const misHabilidades = this.authService.currentUser()?.freelancer?.habilidadResponses || [];
    const idsMisHabilidades = misHabilidades.map(h => h.habilidadId);

    /*const cumpleRequisito = proyecto.habilidadResponses.some(hReq =>
      idsMisHabilidades.includes(hReq.habilidadId)
    );

    if (!cumpleRequisito) {
      this.errorMessage.set('No puedes postularte a este proyecto porque no cuentas con ninguna de las habilidades requeridas en tu perfil.');
      window.scrollTo({ top: 0, behavior: 'smooth' });
      return;
    }*/

    this.proyectoSeleccionado.set(proyecto);
    this.isLoading.set(true);

    this.propuestaService.getAllPropuestaFromAFreelancer(proyecto.proyectoId).subscribe({
      next: (propuestas) => {
        this.isLoading.set(false);
        if (propuestas && propuestas.length > 0) {
          this.miPropuestaActual.set(propuestas[0]);
        } else {
          this.miPropuestaActual.set(null);
        }
        this.vistaActual.set('gestion');
      },
      error: () => {
        this.isLoading.set(false);
        this.errorMessage.set('Error al consultar si ya tenías una propuesta previa.');
      }
    });
  }

  procesarFormulario(formData: any) {
    this.isLoading.set(true);
    const propuestaExistente = this.miPropuestaActual();

    if (propuestaExistente) {
      const updateReq: PropuestaUpdate = {
        propuestaId: propuestaExistente.propuestaId,
        monto: formData.monto,
        tiempoEntrega: formData.tiempoEntrega,
        descripcion: formData.descripcion
      };

      this.propuestaService.updatePropuesta(updateReq).subscribe({
        next: () => {
          this.successMessage.set('¡Tu propuesta ha sido actualizada con éxito!');
          this.finalizarAccion();
        },
        error: (err) => this.manejarError(err)
      });

    } else {
      const req: PropuestaRequest = {
        proyectoId: this.proyectoSeleccionado()!.proyectoId,
        usuarioId: this.authService.currentUser()!.usuarioId,
        monto: formData.monto,
        tiempoEntrega: formData.tiempoEntrega,
        descripcion: formData.descripcion,
        fechaCreacion: new Date().toISOString().split('T')[0]
      };

      this.propuestaService.createPropuesta(req).subscribe({
        next: () => {
          this.successMessage.set('¡Propuesta enviada con éxito!');
          this.finalizarAccion();
        },
        error: (err) => this.manejarError(err)
      });
    }
  }

  retirarMiPropuesta() {
    const propuesta = this.miPropuestaActual();
    if (!propuesta) return;

    if (confirm('¿Estás seguro de retirar tu propuesta? El cliente ya no podrá verla.')) {
      this.isLoading.set(true);
      this.propuestaService.retirarPropuesta(propuesta.propuestaId).subscribe({
        next: () => {
          this.successMessage.set('Has retirado tu propuesta de este proyecto exitosamente.');
          this.finalizarAccion();
        },
        error: (err) => this.manejarError(err)
      });
    }
  }

  cancelarGestion() {
    this.vistaActual.set('lista');
    this.proyectoSeleccionado.set(null);
    this.miPropuestaActual.set(null);
    this.errorMessage.set('');
  }

  private finalizarAccion() {
    this.isLoading.set(false);
    this.vistaActual.set('lista');
    this.proyectoSeleccionado.set(null);
    this.miPropuestaActual.set(null);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  private manejarError(err: any) {
    this.isLoading.set(false);
    this.errorMessage.set(err.error?.error || 'Ocurrió un error en la operación.');
    setTimeout(() => this.errorMessage.set(''), 3000);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }
}