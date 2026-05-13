import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { ProyectoCardComponent } from '../../../components/proyecto-card/proyecto-card.component';
import { ProyectoFormComponent } from '../../../components/proyecto-form/proyecto-form.component';
import { ProyectoService } from '../../../services/proyecto/proyecto.service';
import { CategoriaService } from '../../../services/categoria/categoria.service';
import { HabilidadService } from '../../../services/habilidad/habilidad.service';
import { AuthService } from '../../../services/auth/auth.service';
import { EnumProyecto } from '../../../models/proyecto/EnumProyecto';
import { ProyectoResponse } from '../../../models/proyecto/ProyectoResponse';
import { HabilidadResponse } from '../../../models/habilidad/HabilidadResponse';
import { CategoriaResponse } from '../../../models/categoria/CategoriaResponse';
import { ProyectoUpdate } from '../../../models/proyecto/ProyectoUpdate';
import { ProyectoRequest } from '../../../models/proyecto/ProyectoRequest';
import { EnumUsuario } from '../../../models/usuario/EnumUsuario';
import { Router } from '@angular/router';

@Component({
  selector: 'app-cliente-proyectos.component',
  imports: [CommonModule, ProyectoCardComponent, ProyectoFormComponent],
  templateUrl: './cliente-proyectos.component.html'
})
export class ClienteProyectosComponent implements OnInit {
  private proyectoService = inject(ProyectoService);
  private categoriaService = inject(CategoriaService);
  private habilidadService = inject(HabilidadService);
  private authService = inject(AuthService);
  private router = inject(Router);

  enumUsuario = EnumUsuario;
  enumProyecto = EnumProyecto;
  proyectos = signal<ProyectoResponse[]>([]);
  categorias = signal<CategoriaResponse[]>([]);
  habilidadesDisponibles = signal<HabilidadResponse[]>([]);

  errorMessage = signal<string>('');
  vistaActual = signal<'lista' | 'crear' | 'editar'|'habilidades'>('lista');
  proyectoAEditar = signal<ProyectoResponse | null>(null);
  
  isLoading = signal<boolean>(false);

  ngOnInit() {
    this.cargarProyectos();
    this.cargarCatalogos();
  }

  cargarProyectos() {
    this.proyectoService.getMisProyectos().subscribe({
      next: (res) => this.proyectos.set(res)
    });
  }

  cargarCatalogos() {
    this.categoriaService.getActivadaCategoria().subscribe(res => this.categorias.set(res));
    this.habilidadService.getAllHabilidadActivada().subscribe(res => this.habilidadesDisponibles.set(res));
  }

  buscarProyecto(event: Event) {
    const termino = (event.target as HTMLInputElement).value.trim();
    if (!termino) {
      this.cargarProyectos();
      return;
    }
    this.proyectoService.getMisProyectosByCoincidencia(termino).subscribe({
      next: (res) => this.proyectos.set(res),
      error: () => this.proyectos.set([])
    });
  }

  cambiarVista(vista: 'lista' | 'crear') {
    this.vistaActual.set(vista);
    this.proyectoAEditar.set(null);
  }

  iniciarEdicion(proyecto: ProyectoResponse) {
    this.proyectoAEditar.set(proyecto);
    this.vistaActual.set('editar');
  }

  procesarFormulario(evento: { datosFormulario: any, habilidadesSeleccionadas: number[] }) {
    this.isLoading.set(true);
    const formValue = evento.datosFormulario;

    if (this.vistaActual() === 'editar' && this.proyectoAEditar()) {
      const updateReq: ProyectoUpdate = {
        proyectoId: this.proyectoAEditar()!.proyectoId,
        categoriaId: Number(formValue.categoriaId),
        titulo: formValue.titulo,
        descripcion: formValue.descripcion,
        presupuesto: formValue.presupuesto,
        fechaLimite: formValue.fechaLimite
      };

      this.proyectoService.updateProyecto(updateReq).subscribe({
        next: () => this.finalizarPeticion(),
        error: () => this.isLoading.set(false)
      });

    } else {
      const usuarioId = this.authService.currentUser()!.usuarioId;
      const hoy = new Date().toISOString().split('T')[0];

      const createReq: ProyectoRequest = {
        usuarioId: usuarioId,
        categoriaId: Number(formValue.categoriaId),
        titulo: formValue.titulo,
        descripcion: formValue.descripcion,
        presupuesto: formValue.presupuesto,
        fechaCreacion: hoy,
        fechaLimite: formValue.fechaLimite,
        proyectoHabilidadRequest: evento.habilidadesSeleccionadas.map(id => ({ habilidadId: id }))
      };

      this.proyectoService.createProyecto(createReq).subscribe({
        next: () => this.finalizarPeticion(),
        error: () => this.isLoading.set(false)
      });
    }
  }

  cancelarProyecto(proyecto: ProyectoResponse) {
    if (confirm(`¿Estás seguro de cancelar el proyecto "${proyecto.titulo}"? Esta acción no se puede deshacer.`)) {
      this.proyectoService.cancelarProyecto(proyecto.proyectoId).subscribe({
        next: () => this.cargarProyectos()
      });
    }
  }

  private finalizarPeticion() {
    this.isLoading.set(false);
    this.cambiarVista('lista');
    this.cargarProyectos();
  }

  iniciarGestionHabilidades(proyecto: ProyectoResponse) {
    this.proyectoAEditar.set(proyecto);
    this.vistaActual.set('habilidades');
    this.errorMessage.set('');
  }

  agregarHabilidadAProyecto(habilidadId: number) {
    const proyecto = this.proyectoAEditar();
    if (!proyecto) return;

    this.isLoading.set(true);
    this.proyectoService.agregarHabilidadProyecto(proyecto.proyectoId, { habilidadId }).subscribe({
      next: () => this.refrescarProyectoActual(),
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err.error?.error || 'Error al agregar habilidad');
      }
    });
  }

  quitarHabilidadDeProyecto(habilidadId: number) {
    const proyecto = this.proyectoAEditar();
    if (!proyecto) return;

    if (proyecto.habilidadResponses.length <= 1) {
      this.errorMessage.set('El proyecto debe tener al menos una habilidad requerida. No puedes eliminar la última.');
      return;
    }

    this.isLoading.set(true);
    this.proyectoService.eliminarHabilidadProyecto(proyecto.proyectoId, habilidadId).subscribe({
      next: () => this.refrescarProyectoActual(),
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err.error?.error || 'Error al quitar habilidad');
      }
    });
  }

  private refrescarProyectoActual() {
    const proyectoId = this.proyectoAEditar()?.proyectoId;
    if (!proyectoId) return;

    this.proyectoService.getUsuarioProyectoById(proyectoId).subscribe({
      next: (proyectoActualizado) => {
        this.proyectoAEditar.set(proyectoActualizado);
        this.isLoading.set(false);
        this.cargarProyectos(); 
      }
    });
  }

  verDetalleProyecto(proyecto: ProyectoResponse) {
  this.router.navigate(['/cliente/proyecto-detalle'], { queryParams: { id: proyecto.proyectoId } });
}

  getHabilidadesParaAgregar(): HabilidadResponse[] {
    const proyecto = this.proyectoAEditar();
    if (!proyecto) return [];
    const idsActuales = proyecto.habilidadResponses.map(h => h.habilidadId);
    return this.habilidadesDisponibles().filter(h => !idsActuales.includes(h.habilidadId));
  }
}