import { Component, EventEmitter, Input, Output } from '@angular/core';
import { CatalogoItem } from '../../models/catalogo/CatalogoItem';

@Component({
  selector: 'app-catalogo-card-component',
  imports: [],
  templateUrl: './catalogo-card.component.html'
})
export class CatalogoCardComponent {
  @Input() item!: CatalogoItem;
  @Output() onEdit = new EventEmitter<CatalogoItem>();
  @Output() onToggleEstado = new EventEmitter<CatalogoItem>();
}
