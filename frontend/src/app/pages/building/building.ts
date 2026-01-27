import { Component, computed, effect, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { interval, switchMap, tap } from 'rxjs';
import { toSignal } from '@angular/core/rxjs-interop';
import { ElevatorService } from '../../services/elevator';
import { ElevatorModel } from '../../models/elevator.model';

@Component({
  selector: 'app-building',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './building.html',
  styleUrl: './building.css',
})
export class Building {
  refresh$ = interval(250);
elevators = toSignal(
    this.refresh$.pipe(
      switchMap(() => this.elevatorService.getAll()),
      tap(() => this.updateProgress())
    ),
    { initialValue: [] as ElevatorModel[] }
  );
  doorProgress = signal<Record<number, number>>({});
  selectedFloor: Record<number, number> = {};
  callFloor: Record<number, number> = {};
  callDirection: Record<number, 'UP' | 'DOWN'> = {};

  minFloor = computed(() =>
    this.elevators().length ? this.elevators()[0].minFloor : 0
  );

  maxFloor = computed(() =>
    this.elevators().length ? this.elevators()[0].maxFloor : 0
  );

  floors = computed(() => {
    if (!this.elevators().length) return [];
    const min = this.minFloor();
    const max = this.maxFloor();
    return Array.from(
      { length: max - min + 1 },
      (_, i) => max - i
    );
  });



  constructor(
    private elevatorService: ElevatorService
  ) {}

  updateProgress() {
  const updated: Record<number, number> = { ...this.doorProgress() };

  for (const e of this.elevators()) {
    const current = updated[e.id] ?? 0;

    if (e.doorState === 'OPENING') {
      updated[e.id] = Math.min(100, current + 10);
    }
    else if (e.doorState === 'CLOSING') {
      updated[e.id] = Math.max(0, current - 10);
    }
    else if (e.doorState === 'OPEN') {
      updated[e.id] = Math.max(current, 100);
    }
    else if (e.doorState === 'CLOSED') {
      updated[e.id] = Math.min(current, 0);
    }
  }

  this.doorProgress.set(updated);
}

targetFloor(e: ElevatorModel): number[] | null {
  return e.requests?.length ? e.requests : null;
}

  select(id: number, floor: number) {
    this.elevatorService.select(id, floor).subscribe(() => {});
  }

  call(id: number) {
    const floor = this.callFloor[id];
    const direction = this.callDirection[id];
    this.elevatorService.call(id, floor, direction).subscribe(() => {});
  }

  openDoors(id: number) {
    this.elevatorService.openDoors(id).subscribe(() => {});
  }

  closeDoors(id: number) {
    this.elevatorService.closeDoors(id).subscribe(() => {});
  }

}
