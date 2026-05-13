import { CommonModule } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ConfiguracionService } from '../../../services/configuracionSistema/configuracion.service';
import { ConfiguracionSistemaResponse } from '../../../models/configuracionSistema/ConfiguracionSistemaResponse';
import { ConfiguracionSistemaRequest } from '../../../models/configuracionSistema/ConfiguracionSistemaRequest';

@Component({
  selector: 'app-admin-dashboard.component',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './admin-dashboard.component.html'
})
export class AdminDashboardComponent implements OnInit {
  private fb = inject(FormBuilder);
  private configService = inject(ConfiguracionService);

  historial = signal<ConfiguracionSistemaResponse[]>([]);
  configActual = signal<ConfiguracionSistemaResponse | null>(null);
  
  isLoading = signal<boolean>(false);
  errorMessage = signal<string>('');
  successMessage = signal<string>('');

  configForm: FormGroup = this.fb.group({
    comision: ['', [Validators.required, Validators.min(0), Validators.max(100)]],
    fechaInicio: ['', [Validators.required]]
  });

  ngOnInit() {
    this.cargarDatos();
  }

  cargarDatos() {
    this.configService.getUltimo().subscribe({
      next: (res) => this.configActual.set(res),
      error: () => this.configActual.set(null)
    });

    this.configService.getAll().subscribe({
      next: (res) => this.historial.set(res)
    });
  }

  onSubmit() {
    if (this.configForm.invalid) {
      this.configForm.markAllAsTouched();
      return;
    }

    this.isLoading.set(true);
    this.errorMessage.set('');
    this.successMessage.set('');

    this.configService.create(this.configForm.value as ConfiguracionSistemaRequest).subscribe({
      next: () => {
        this.isLoading.set(false);
        this.successMessage.set('Configuración actualizada correctamente.');
        this.configForm.reset();
        this.cargarDatos(); // Recargamos las tablas
      },
      error: (err) => {
        this.isLoading.set(false);
        this.errorMessage.set(err.error?.error || 'Error al guardar configuración.');
      }
    });
  }}
