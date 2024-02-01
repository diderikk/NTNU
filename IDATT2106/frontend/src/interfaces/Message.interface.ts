import IUserInMessage from "./User/UserInMessage.interface";

export default interface Message {
    messageId:number
    message:String
    timeSent:String
    user:IUserInMessage
}