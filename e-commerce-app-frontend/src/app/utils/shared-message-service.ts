import { Injectable } from '@angular/core';
import { MessageService } from 'primeng/api';

@Injectable({
  providedIn: 'root'
})
export class SharedMessageService {

  constructor(private messageService: MessageService) { }

  addMessage(severity: string, summary: string, detail: string, life: number = 4000) {
    this.messageService.add({ severity, summary, detail, life });
  }

  clearMessages() {
    this.messageService.clear();
  }
}
