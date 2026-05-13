import { CommonModule, DecimalPipe } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { PropuestaDetalleResponse } from '../../models/propuesta/PropuestaDetalleResponse';
import { EnumPropuesta } from '../../models/propuesta/EnumPropuesta';
import { PropuestaResponse } from '../../models/propuesta/PropuestaResponse';

@Component({
  selector: 'app-propuesta-card-component',
  imports: [CommonModule, DecimalPipe],
  templateUrl: './propuesta-card.component.html'
})
export class PropuestaCardComponent {
  @Input({ required: true }) propuesta!: PropuestaResponse | PropuestaDetalleResponse;
  
  @Input() vistaRol: 'cliente' | 'freelancer' = 'freelancer';
  
  @Output() onVerDetalle = new EventEmitter<any>();
  @Output() onAceptar = new EventEmitter<number>();
  @Output() onRechazar = new EventEmitter<number>();

  enumPropuesta = EnumPropuesta;

  isDetalle(p: any): p is PropuestaDetalleResponse {
    return 'nombreCompleto' in p;
  }
}
