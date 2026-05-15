import { Component, EventEmitter, Input, Output } from '@angular/core';
import { EnumContrato } from '../../models/contrato/EnumContrato';
import { ContratoResponse } from '../../models/contrato/ContratoResponse';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-contrato-card-component',
  imports: [CommonModule],
  templateUrl: './contrato-card.component.html'
})
export class ContratoCardComponent {
  @Input({ required: true }) contrato!: ContratoResponse;
  @Output() onVerDetalle = new EventEmitter<number>();

  enumContrato = EnumContrato;
}
