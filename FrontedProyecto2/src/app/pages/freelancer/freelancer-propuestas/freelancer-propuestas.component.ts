import { CommonModule } from '@angular/common';
import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { PropuestaCardComponent } from '../../../components/propuesta-card/propuesta-card.component';
import { PropuestaService } from '../../../services/propuesta/propuesta.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-freelancer-propuestas.component',
  imports: [CommonModule, PropuestaCardComponent],
  templateUrl: './freelancer-propuestas.component.html'
})
export class FreelancerPropuestasComponent implements OnInit {
  private propuestaService = inject(PropuestaService);
  private router = inject(Router);

  propuestas = signal<any[]>([]);
  filtroActual = signal<string>('TODAS');
  isLoading = signal<boolean>(false);

  ngOnInit() {
    this.cargarHistorial();
  }

  cargarHistorial() {
    this.isLoading.set(true);
    this.propuestaService.getHistorialPropuestas(this.filtroActual()).subscribe({
      next: (res) => {
        this.propuestas.set(res);
        this.isLoading.set(false);
      },
      error: () => this.isLoading.set(false)
      });
  }

  cambiarFiltro(nuevoFiltro: string) {
    this.filtroActual.set(nuevoFiltro);
    this.cargarHistorial();
  }

  verProyecto(proyectoId: number) {
    this.router.navigate(['/freelancer/buscar-proyectos'], { queryParams: { id: proyectoId } });
  }
}