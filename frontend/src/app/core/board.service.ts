import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Board, CreateBoard, CreateCard } from './models';

@Injectable({ providedIn: 'root' })
export class BoardService {
  constructor(private readonly http: HttpClient) {}

  list(): Observable<Board[]> {
    return this.http.get<Board[]>('/api/boards');
  }

  create(board: CreateBoard): Observable<Board> {
    return this.http.post<Board>('/api/boards', board);
  }

  update(id: number, board: CreateBoard): Observable<Board> {
    return this.http.put<Board>(`/api/boards/${id}`, board);
  }

  createCard(boardId: number, columnId: number, card: CreateCard): Observable<void> {
    return this.http.post<void>(`/api/boards/${boardId}/columns/${columnId}/cards`, card);
  }

  updateCard(boardId: number, columnId: number, cardId: number, card: CreateCard): Observable<void> {
    return this.http.put<void>(`/api/boards/${boardId}/columns/${columnId}/cards/${cardId}`, card);
  }
}
