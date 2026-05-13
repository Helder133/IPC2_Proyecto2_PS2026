import { Component, inject, OnInit, signal } from "@angular/core";
import { CatalogoCardComponent } from "../../../components/catalogo-card/catalogo-card.component";
import { CatalogoFormComponent } from "../../../components/catalogo-form/catalogo-form.component";
import { CategoriaService } from "../../../services/categoria/categoria.service";
import { CatalogoEnum } from "../../../models/catalogo/CatalogoEnum";
import { CatalogoItem } from "../../../models/catalogo/CatalogoItem";
import { CategoriaUpdate } from "../../../models/categoria/CategoriaUpdate";
import { CategoriaRequest } from "../../../models/categoria/CategoriaRequest";


@Component({
  selector: 'app-admin-categorias.component',
  imports: [CatalogoFormComponent, CatalogoCardComponent],
  templateUrl: './admin-categorias.component.html'
})
export class AdminCategoriasComponent implements OnInit {
  private categoriaService = inject(CategoriaService);

  catalogoEnum = CatalogoEnum;
  categorias = signal<CatalogoItem[]>([]);
  isLoading = signal<boolean>(false);
  
  isEditing = signal<boolean>(false);
  itemAEditar = signal<any>(null);
  errorMessage = signal<string>('');

  ngOnInit() {
    this.cargarCategorias();
  }

  cargarCategorias() {
    this.categoriaService.getAllCategorias().subscribe({
      next: (res) => {
        const mapped: CatalogoItem[] = res.map(c => ({
          id: c.categoriaId,
          nombre: c.nombre,
          descripcion: c.descripcion,
          estado: c.estado,
          tipo: CatalogoEnum.Categoria
        }));
        this.categorias.set(mapped);
      }
    });
  }

  procesarFormulario(evento: { tipo: CatalogoEnum, datos: any }) {
    this.isLoading.set(true);
    this.errorMessage.set('');

    if (this.isEditing()) {
      const updateReq: CategoriaUpdate = {
        categoriaId: evento.datos.id,
        nombre: evento.datos.nombre,
        descripcion: evento.datos.descripcion
      };
      
      this.categoriaService.actualizarCategoria(updateReq).subscribe({
        next: () => {
          this.isLoading.set(false);
          this.cancelarEdicion();
          this.cargarCategorias();
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
      const createReq: CategoriaRequest = {
        nombre: evento.datos.nombre,
        descripcion: evento.datos.descripcion
      };

      this.categoriaService.createCategoria(createReq).subscribe({
        next: () => {
          this.isLoading.set(false);
          this.cargarCategorias();
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

  buscarCategoria(event: Event) {
    const termino = (event.target as HTMLInputElement).value.trim();
    
    if (!termino) {
      this.cargarCategorias();
      return;
    }

    this.categoriaService.getCategoriaByCoincidence(termino).subscribe({
      next: (res) => {
        const mapped: CatalogoItem[] = res.map(c => ({
          id: c.categoriaId,
          nombre: c.nombre,
          descripcion: c.descripcion,
          estado: c.estado,
          tipo: this.catalogoEnum.Categoria
        }));
        this.categorias.set(mapped);
      },
      error: () => this.categorias.set([])
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
    this.categoriaService.actualizarCategoriaEstado(item.id).subscribe({
      next: () => this.cargarCategorias()
    });
  }
}
