import { Component, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AuthService } from '../core/auth.service';
import { BoardService } from '../core/board.service';
import { Board, BoardColumn, BoardSummary, Card, CreateBoard, CreateCard } from '../core/models';
import { BoardCanvasComponent } from './board-canvas.component';
import { BoardFormModalComponent, EditableColumn } from './board-form-modal.component';
import { CardFormModalComponent } from './card-form-modal.component';

@Component({
  selector: 'app-board-page',
  imports: [BoardCanvasComponent, BoardFormModalComponent, CardFormModalComponent, RouterLink],
  templateUrl: './board-page.component.html',
  styleUrl: './board-page.component.scss'
})
export class BoardPageComponent implements OnInit {
  readonly boards = signal<BoardSummary[]>([]);
  readonly selectedBoardId = signal<number | null>(null);
  readonly selectedBoard = signal<Board | null>(null);
  readonly isLoading = signal(true);
  readonly isLoadingBoard = signal(false);
  readonly isCreating = signal(false);
  readonly isEditing = signal(false);
  readonly isSaving = signal(false);
  readonly isCreatingCard = signal(false);
  readonly isEditingCard = signal(false);
  readonly isSavingCard = signal(false);
  readonly error = signal('');
  readonly loadError = signal('');
  readonly cardError = signal('');
  readonly cardColumn = signal<BoardColumn | null>(null);
  readonly editingCardId = signal<number | null>(null);
  name = '';
  description = '';
  cardTitle = '';
  cardDescription = '';
  cardColumnId: number | null = null;
  columns: EditableColumn[] = this.defaultColumns();

  constructor(
    readonly auth: AuthService,
    private readonly boardService: BoardService
  ) {}

  ngOnInit(): void {
    this.loadBoards();
  }

  selectBoard(id: number): void {
    this.selectedBoardId.set(id);
    this.selectedBoard.set(null);
    this.loadBoard(id);
  }

  openCreate(): void {
    this.error.set('');
    this.isEditing.set(false);
    this.resetForm();
    this.isCreating.set(true);
  }

  openEdit(board: Board): void {
    this.error.set('');
    this.name = board.name;
    this.description = board.description;
    this.columns = (board.columns ?? []).map(({ id, nome, active, posicao }, index) => ({
      id,
      nome,
      active,
      posicao: posicao ?? index + 1
    }));
    this.isEditing.set(true);
    this.isCreating.set(true);
  }

  closeCreate(): void {
    this.isCreating.set(false);
    this.isEditing.set(false);
    this.error.set('');
    this.resetForm();
  }

  openCreateCard(column: BoardColumn): void {
    this.cardColumn.set(column);
    this.cardColumnId = column.id ?? null;
    this.cardTitle = '';
    this.cardDescription = '';
    this.cardError.set('');
    this.isEditingCard.set(false);
    this.isCreatingCard.set(true);
  }

  openEditCard(column: BoardColumn, card: Card): void {
    if (!card.id || !column.id) {
      return;
    }

    this.cardColumn.set(column);
    this.cardColumnId = column.id;
    this.editingCardId.set(card.id);
    this.cardTitle = card.titulo;
    this.cardDescription = card.descricao;
    this.cardError.set('');
    this.isEditingCard.set(true);
    this.isCreatingCard.set(true);
  }

  closeCreateCard(): void {
    this.isCreatingCard.set(false);
    this.isEditingCard.set(false);
    this.cardColumn.set(null);
    this.editingCardId.set(null);
    this.cardError.set('');
    this.cardTitle = '';
    this.cardDescription = '';
    this.cardColumnId = null;
  }

  saveCard(): void {
    const board = this.selectedBoard();
    const title = this.cardTitle.trim();
    const description = this.cardDescription.trim();
    const columnId = this.cardColumnId;

    if (!board || !columnId) {
      this.cardError.set('Não foi possível identificar a coluna selecionada.');
      return;
    }

    if (!title || !description) {
      this.cardError.set('Informe o título e a descrição do card.');
      return;
    }

    this.isSavingCard.set(true);
    this.cardError.set('');
    const card = { titulo: title, descricao: description };
    const editingCardId = this.editingCardId();
    const request = this.isEditingCard() && editingCardId
      ? this.boardService.updateCard(board.id, columnId, editingCardId, card)
      : this.boardService.createCard(board.id, columnId, card);

    request.subscribe({
      next: () => {
        this.isSavingCard.set(false);
        if (editingCardId) {
          this.updateCardInBoard(board.id, editingCardId, columnId, card);
        } else {
          this.loadBoard(board.id);
        }
        this.closeCreateCard();
      },
      error: (response) => {
        this.isSavingCard.set(false);
        this.cardError.set(
          response.error?.message ??
            (this.isEditingCard() ? 'Não foi possível atualizar o card.' : 'Não foi possível criar o card.')
        );
      }
    });
  }

  cardsForColumn(column: BoardColumn): Card[] {
    return column.cards ?? [];
  }

  addColumn(): void {
    this.columns = [
      ...this.columns,
      { nome: 'Nova coluna', active: true, posicao: this.columns.length + 1 }
    ];
  }

  removeColumn(index: number): void {
    if (this.columns[index].active && this.columns.filter((column) => column.active).length > 1) {
      this.columns = this.columns.map((column, currentIndex) =>
        currentIndex === index ? { ...column, active: false } : column
      );
    }
  }

  updateColumn(index: number, changes: Partial<EditableColumn>): void {
    this.columns = this.columns.map((column, currentIndex) =>
      currentIndex === index ? { ...column, ...changes } : column
    );
  }

  saveBoard(): void {
    
    const columns = this.columns
      .map((column) => ({ ...column, nome: column.nome.trim() }))
      .filter((column) => Boolean(column.nome));

    if (!this.name.trim() || !this.description.trim() || !columns.length) {
      this.error.set('Informe o nome, a descrição e pelo menos uma coluna.');
      return;
    }

    if (columns.some((column) => !Number.isInteger(column.posicao) || column.posicao < 1)) {
      this.error.set('Informe uma posição inteira maior ou igual a 1 para cada coluna.');
      return;
    }

    const board: CreateBoard = {
      name: this.name.trim(),
      description: this.description.trim(),
      active: true,
      columns
    };
    this.isSaving.set(true);
    this.error.set('');
    const selectedBoard = this.selectedBoard();
    const request = this.isEditing() && selectedBoard
      ? this.boardService.update(selectedBoard.id, board)
      : this.boardService.create(board);

    request.subscribe({
      next: (saved) => {
        const summary: BoardSummary = {
          id: saved.id,
          name: saved.name,
          description: saved.description,
          active: saved.active,
          createdAt: saved.createdAt,
          updatedAt: saved.updatedAt
        };
        this.boards.update((boards) =>
          this.isEditing()
            ? boards.map((board) => (board.id === saved.id ? summary : board))
            : [...boards, summary]
        );
        this.selectedBoardId.set(saved.id);
        this.selectedBoard.set(saved);
        this.isLoadingBoard.set(false);
        this.isSaving.set(false);
        this.closeCreate();
      },
      error: (response) => {
        this.isSaving.set(false);
        this.error.set(
          response.error?.message ??
            (this.isEditing() ? 'Não foi possível atualizar o quadro.' : 'Não foi possível criar o quadro.')
        );
      }
    });
  }

  loadBoards(): void {
    this.boardService.list().subscribe({
      next: (boards) => {
        this.boards.set(boards);
        this.loadError.set('');
        this.isLoading.set(false);
      },
      error: (response) => {
        this.loadError.set(response.error?.message ?? 'Não foi possível carregar os quadros.');
        this.isLoading.set(false);
      }
    });
  }

  private loadBoard(id: number): void {
    this.isLoadingBoard.set(true);
    this.loadError.set('');
    this.boardService.getById(id).subscribe({
      next: (board) => {
        if (this.selectedBoardId() === id) {
          this.selectedBoard.set(board);
          this.isLoadingBoard.set(false);
        }
      },
      error: (response) => {
        if (this.selectedBoardId() === id) {
          this.loadError.set(response.error?.message ?? 'Não foi possível carregar o quadro.');
          this.isLoadingBoard.set(false);
        }
      }
    });
  }

  private updateCardInBoard(boardId: number, cardId: number, columnId: number, changes: CreateCard): void {
    const board = this.selectedBoard();
    if (board?.id === boardId) {
      this.selectedBoard.set(this.updateCardColumns(board, cardId, columnId, changes));
    }
  }

  private updateCardColumns(board: Board, cardId: number, columnId: number, changes: CreateCard): Board {
    const columns = board.columns ?? [];
    const card = columns
      .flatMap((column) => this.cardsForColumn(column))
      .find((currentCard) => currentCard.id === cardId);

    if (!card) {
      return board;
    }

    return {
      ...board,
      columns: columns.map((column) => {
        const cards = this.cardsForColumn(column);

        if (column.id === columnId) {
          return {
            ...column,
            cards: cards.some((currentCard) => currentCard.id === cardId)
              ? cards.map((currentCard) =>
                  currentCard.id === cardId
                    ? { ...currentCard, ...changes, columnBoardId: columnId }
                    : currentCard
                )
              : [...cards, { ...card, ...changes, columnBoardId: columnId }]
          };
        }

        return { ...column, cards: cards.filter((currentCard) => currentCard.id !== cardId) };
      })
    };
  }

  private resetForm(): void {
    this.name = '';
    this.description = '';
    this.columns = this.defaultColumns();
  }

  private defaultColumns(): EditableColumn[] {
    return ['A fazer', 'Em andamento', 'Concluído'].map((nome, index) => ({
      nome,
      active: true,
      posicao: index + 1
    }));
  }

}
