export interface ElevatorModel {
  id: number;
  currentFloor: number;
  minFloor: number;
  maxFloor: number;
  status: 'OPERATING' | 'NOT_OPERATING';
  direction: 'MOVING_UP' | 'MOVING_DOWN' | 'NOT_MOVING';
  doorState: 'OPEN' | 'CLOSED' | 'OPENING' | 'CLOSING';
  requests: number[];
}
