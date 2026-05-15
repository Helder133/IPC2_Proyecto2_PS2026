import { Component, EventEmitter, Input, Output } from '@angular/core';
import { EnumNueva_h_cTipo } from '../../models/nueva_h_c/EnumNueva_h_cTipo';
import { EnumNueva_h_cEstado } from '../../models/nueva_h_c/EnumNueva_h_cEstado';
import { Nueva_h_cResponse } from '../../models/nueva_h_c/Nueva_h_cResponse';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-solicitud-card-component',
  imports: [CommonModule],
  templateUrl: './solicitud-card.component.html'
})
export class SolicitudCardComponent {
  @Input({ required: true }) solicitud!: Nueva_h_cResponse;
  @Input() vistaRol: 'usuario' | 'admin' = 'usuario';
  @Output() onEditar = new EventEmitter<Nueva_h_cResponse>();
  @Output() onAceptar = new EventEmitter<number>();
  @Output() onRechazar = new EventEmitter<number>();

  enumEstado = EnumNueva_h_cEstado;
  enumTipo = EnumNueva_h_cTipo;
}
