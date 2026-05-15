import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { EnumEntrega } from '../../models/entrega/EnumEntrega';
import { EntregaResponse } from '../../models/entrega/EntregaResponse';

@Component({
  selector: 'app-entrega-card-component',
  imports: [CommonModule],
  templateUrl: './entrega-card.component.html'
})
export class EntregaCardComponent {
  @Input({ required: true }) entrega!: EntregaResponse;
  @Input() vistaRol: 'cliente' | 'freelancer' = 'freelancer';

  @Output() onAprobar = new EventEmitter<number>();
  @Output() onRechazar = new EventEmitter<number>();

  enumEntrega = EnumEntrega;
}
