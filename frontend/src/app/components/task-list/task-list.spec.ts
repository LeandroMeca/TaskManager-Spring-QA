import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TaskList } from './task-list';
import { of } from 'rxjs';
import { TaskService } from '../../services/task-service';

describe('TaskList', () => {
  let component: TaskList;
  let fixture: ComponentFixture<TaskList>;

  const mockTaskService = {
    getTasks: () =>
      of([
        { id: 1, title: 'Tarefa Mockada', description: 'Apenas para o teste', completed: false },
      ]),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TaskList],
      providers: [{ provide: TaskService, useValue: mockTaskService }],
    }).compileComponents();

    fixture = TestBed.createComponent(TaskList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('deve criar o componente', () => {
    expect(component).toBeTruthy();
  });

  it('deve carregar a lista mockada ao iniciar', () => {
    expect(component.tasks.length).toBe(1);
    expect(component.tasks[0].title).toBe('Tarefa Mockada');
  });
});
