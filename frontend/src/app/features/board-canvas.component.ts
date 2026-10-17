import { CdkDropList, CdkDropListGroup } from '@angular/cdk/drag-drop';
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { orderedActiveColumns } from '../core/board-utils';
import { Board, BoardColumn, Card } from '../core/models';

@Component({
  selector: 'app-board-canvas',
  standalone: true,
  imports: [CdkDropList, CdkDropListGroup],
  templateUrl: './board-canvas.component.html',
  styleUrl: './board-canvas.component.scss'
})
export class BoardCanvasComponent {
  @Input({ required: true }) board!: Board;
  @Input() isLoading = false;

  @Output() editBoard = new EventEmitter<Board>();
  @Output() createBoard = new EventEmitter<void>();
  @Output() createCard = new EventEmitter<BoardColumn>();
  @Output() editCard = new EventEmitter<{ column: BoardColumn; card: Card }>();

  cardsForColumn(column: BoardColumn): Card[] {
    return column.cards ?? [];
  }

  orderedColumns(columns: BoardColumn[] = []): BoardColumn[] {
    return orderedActiveColumns(columns);
  }
}
