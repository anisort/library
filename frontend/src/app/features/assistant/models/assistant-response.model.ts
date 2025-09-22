interface AssistantResponse {
  id: number;
  response: string;
  promptTokens: number;
  completionTokens: number;
  totalTokens: number;
  createdOn: string;
}
