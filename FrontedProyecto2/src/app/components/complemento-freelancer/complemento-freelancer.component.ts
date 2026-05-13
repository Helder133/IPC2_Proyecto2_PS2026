import { Component, EventEmitter, inject, Input, OnInit, Output, signal } from '@angular/core';
import { FreelancerRequest } from '../../models/usuario/freelancer/FreelancerRequest';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { HabilidadService } from '../../services/habilidad/habilidad.service';
import { HabilidadResponse } from '../../models/habilidad/HabilidadResponse';
import { EnumFreelancer } from '../../models/usuario/freelancer/EnumFreelancer';

@Component({
  selector: 'app-complemento-freelancer-component',
  imports: [ReactiveFormsModule],
  templateUrl: './complemento-freelancer.component.html'
})
export class ComplementoFreelancerComponent implements OnInit {
  private fb = inject(FormBuilder);
  private habilidadService = inject(HabilidadService);

  @Input() isLoading: boolean = false;
  @Output() formSubmit = new EventEmitter<FreelancerRequest>();

  habilidades = signal<HabilidadResponse[]>([]);
  habilidadesSeleccionadas = new Set<number>();
  EnumFreelancer = EnumFreelancer;

  freelancerForm: FormGroup = this.fb.group({
    descripcion: ['', [Validators.required, Validators.maxLength(200)]],
    experiencia: ['', [Validators.required]],
    tarifaHora: ['', [Validators.required, Validators.min(1)]]
  });

  ngOnInit() {
    this.habilidadService.getAllHabilidadActivada().subscribe(res => this.habilidades.set(res));
  }

  toggleHabilidad(id: number, event: any) {
    if (event.target.checked) {
      this.habilidadesSeleccionadas.add(id);
    } else {
      this.habilidadesSeleccionadas.delete(id);
    }
  }

  submitForm() {
    if (this.freelancerForm.invalid || this.habilidadesSeleccionadas.size === 0) {
      this.freelancerForm.markAllAsTouched();
      return;
    }

    // Mapeamos el Set al formato del DTO (List<FreelancerHabilidadRequest>)
    const request: FreelancerRequest = {
      ...this.freelancerForm.value,
      habilidadesRequest: Array.from(this.habilidadesSeleccionadas).map(id => ({ habilidadId: id }))
    };

    this.formSubmit.emit(request);
  }
}