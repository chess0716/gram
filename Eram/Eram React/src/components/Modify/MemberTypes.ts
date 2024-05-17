export interface Member {
  mno: string;
  id: string;
  password: string;
  email: string;
  name?: string;
  mobile?: string;
  fromSocial?: boolean;
  regDate?: Date;
  modDate?: Date;
  roleSet?: MembersRole[];
}

export enum MembersRole {
  USER = "USER",
  ADMIN = "ADMIN",
}
