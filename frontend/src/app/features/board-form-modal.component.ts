import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

export interface EditableColumn {
  id?: number;
  nome: string;
  active: boolean;
  posicao: number;
}

@Component({
  selector: 'app-board-form-modal',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './board-form-modal.component.html',
  styleUrl: './board-form-modal.component.scss'
})
export class BoardFormModalComponent {
  @Input() isEditing = false;
  @Input() isSaving = false;
  @Input() name = '';
  @Input() description = '';
  @Input() columns: EditableColumn[] = [];
  @Input() error = '';

  @Output() nameChange = new EventEmitter<string>();
  @Output() descriptionChange = new EventEmitter<string>();
  @Output() columnChange = new EventEmitter<{ index: number; changes: Partial<EditableColumn> }>();
  @Output() addColumn = new EventEmitter<void>();
  @Output() removeColumn = new EventEmitter<number>();
  @Output() save = new EventEmitter<void>();
  @Output() close = new EventEmitter<void>();
}
