

export interface Member {
  mno: number;
  id: string;
  name: string;
  email: string;
}

export interface Message {
  id: string;
  message: string;
  userId: string;
  name: string;
  direction: "outgoing" | "incoming";
}
