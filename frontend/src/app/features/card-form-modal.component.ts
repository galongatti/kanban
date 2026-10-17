import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { orderedActiveColumns } from '../core/board-utils';
import { BoardColumn } from '../core/models';

@Component({
  selector: 'app-card-form-modal',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './card-form-modal.component.html',
  styleUrl: './card-form-modal.component.scss'
})
export class CardFormModalComponent {
  @Input() isEditing = false;
  @Input() isSaving = false;
  @Input() title = '';
  @Input() description = '';
  @Input() column: BoardColumn | null = null;
  @Input() columns: BoardColumn[] = [];
  @Input() columnId: number | null = null;
  @Input() error = '';

  @Output() titleChange = new EventEmitter<string>();
  @Output() descriptionChange = new EventEmitter<string>();
  @Output() columnIdChange = new EventEmitter<number | null>();
  @Output() save = new EventEmitter<void>();
  @Output() close = new EventEmitter<void>();

  orderedColumns(columns: BoardColumn[]): BoardColumn[] {
    return orderedActiveColumns(columns);
  }
}
