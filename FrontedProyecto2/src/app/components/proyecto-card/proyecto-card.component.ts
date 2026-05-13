import { Component, EventEmitter, Input, Output } from '@angular/core';
import { EnumProyecto } from '../../models/proyecto/EnumProyecto';
import { ProyectoResponse } from '../../models/proyecto/ProyectoResponse';
import { EnumUsuario } from '../../models/usuario/EnumUsuario';
import { CommonModule, DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-proyecto-card-component',
  imports: [DecimalPipe,CommonModule],
  templateUrl: './proyecto-card.component.html'
})
export class ProyectoCardComponent {
  @Input({ required: true }) proyecto!: ProyectoResponse;
  enumusuario = EnumUsuario;
  @Input() vistaRol: EnumUsuario.Cliente | EnumUsuario.Freelancer = EnumUsuario.Cliente;

  @Output() onEdit = new EventEmitter<ProyectoResponse>();
  @Output() onCancel = new EventEmitter<ProyectoResponse>();
  @Output() onVerDetalle = new EventEmitter<ProyectoResponse>();
  @Output() onManageSkills = new EventEmitter<ProyectoResponse>();
  @Output() onPostular = new EventEmitter<ProyectoResponse>();
  enumProyecto = EnumProyecto;
}
