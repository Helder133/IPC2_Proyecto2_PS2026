import { Component, inject, OnInit, signal } from '@angular/core';
import { HabilidadService } from '../../../services/habilidad/habilidad.service';
import { CatalogoItem } from '../../../models/catalogo/CatalogoItem';
import { HabilidadUpdate } from '../../../models/habilidad/HabilidadUpdate';
import { CatalogoEnum } from '../../../models/catalogo/CatalogoEnum';
import { CatalogoFormComponent } from "../../../components/catalogo-form/catalogo-form.component";
import { CatalogoCardComponent } from "../../../components/catalogo-card/catalogo-card.component";
import { HabilidadRequest } from '../../../models/habilidad/HabilidadRequest';

@Component({
  selector: 'app-admin-habilidades.component',
  imports: [CatalogoFormComponent, CatalogoCardComponent],
  templateUrl: './admin-habilidades.component.html'
})
export class AdminHabilidadesComponent implements OnInit {
  private habilidadService = inject(HabilidadService);

  habilidades = signal<CatalogoItem[]>([]);
  isLoading = signal<boolean>(false);
  errorMessage = signal<string>('');
  isEditing = signal<boolean>(false);
  itemAEditar = signal<any>(null);

  ngOnInit() {
    this.cargarHabilidades();
  }

  CatalogoEnum = CatalogoEnum;

  cargarHabilidades() {
    this.habilidadService.getAllHabilidad().subscribe({
      next: (res) => {
        const mapped: CatalogoItem[] = res.map(h => ({
          id: h.habilidadId,
          nombre: h.nombre,
          descripcion: h.descripcion,
          estado: h.estado,
          tipo: CatalogoEnum.Habilidad
        }));
        this.habilidades.set(mapped);
      }
    });
  }

  procesarFormulario(evento: { tipo: CatalogoEnum, datos: any }) {
    this.isLoading.set(true);
    this.errorMessage.set('');
    if (this.isEditing()) {
      const updateReq: HabilidadUpdate = {
        habilidadId: evento.datos.id,
        nombre: evento.datos.nombre,
        descripcion: evento.datos.descripcion
      };

      this.habilidadService.updateHabilidad(updateReq).subscribe({
        next: () => {
          this.isLoading.set(false);
          this.cancelarEdicion();
          this.cargarHabilidades();
        },
        error: (err) => {
          this.isLoading.set(false);
          this.errorMessage.set(err.error?.error || 'Ocurrió un error inesperado en el servidor.');
          setTimeout(() => {
            this.errorMessage.set('');
          }, 3000);
        }
      });
    } else {
      const createReq: HabilidadRequest = {
        nombre: evento.datos.nombre,
        descripcion: evento.datos.descripcion
      };
      this.habilidadService.createHabilidad(createReq).subscribe({
        next: () => {
          this.isLoading.set(false);
          this.cargarHabilidades();
        },
        error: (err) => {
          this.isLoading.set(false);
          this.errorMessage.set(err.error?.error || 'Ocurrió un error inesperado en el servidor.');
          setTimeout(() => {
            this.errorMessage.set('');
          }, 3000);
        }
      });
    }
  }

  buscarHabilidad(event: Event) {
    const termino = (event.target as HTMLInputElement).value.trim();
    if (!termino) {
      this.cargarHabilidades();
      return;
    }

    this.habilidadService.getHabilidadByCoincidence(termino).subscribe({
      next: (res: any) => {
        const data = Array.isArray(res) ? res : [res];

        const mapped: CatalogoItem[] = data.map((h: any) => ({
          id: h.habilidadId,
          nombre: h.nombre,
          descripcion: h.descripcion,
          estado: h.estado,
          tipo: this.CatalogoEnum.Habilidad
        }));
        this.habilidades.set(mapped);
      },
      error: () => this.habilidades.set([])
    });
  }

  iniciarEdicion(item: CatalogoItem) {
    this.isEditing.set(true);
    this.itemAEditar.set(item);
  }

  cancelarEdicion() {
    this.isEditing.set(false);
    this.itemAEditar.set(null);
  }

  cambiarEstado(item: CatalogoItem) {
    this.habilidadService.updateHabilidadEstado(item.id).subscribe({
      next: () => this.cargarHabilidades()
    });
  }
}