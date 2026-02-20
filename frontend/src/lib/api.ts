const API_BASE = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080";

export interface Task {
  id: number;
  title: string;
  description: string;
  status: "TODO" | "IN_PROGRESS" | "DONE";
  priority: "LOW" | "MEDIUM" | "HIGH";
  createdAt: string;
  updatedAt: string;
}

export async function getTasks(status?: string): Promise<Task[]> {
  const url = status
    ? `${API_BASE}/api/tasks?status=${status}`
    : `${API_BASE}/api/tasks`;
  const res = await fetch(url);
  if (!res.ok) throw new Error(`Failed to fetch tasks: ${res.status}`);
  return res.json();
}

export async function getTask(id: number): Promise<Task> {
  const res = await fetch(`${API_BASE}/api/tasks/${id}`);
  if (!res.ok) throw new Error(`Failed to fetch task ${id}: ${res.status}`);
  return res.json();
}

export async function createTask(
  task: Omit<Task, "id" | "createdAt" | "updatedAt">,
): Promise<Task> {
  const res = await fetch(`${API_BASE}/api/tasks`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(task),
  });
  if (!res.ok) throw new Error(`Failed to create task: ${res.status}`);
  return res.json();
}

export async function updateTask(
  id: number,
  task: Omit<Task, "id" | "createdAt" | "updatedAt">,
): Promise<Task> {
  const res = await fetch(`${API_BASE}/api/tasks/${id}`, {
    method: "PUT",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(task),
  });
  if (!res.ok) throw new Error(`Failed to update task ${id}: ${res.status}`);
  return res.json();
}

export async function deleteTask(id: number): Promise<void> {
  const res = await fetch(`${API_BASE}/api/tasks/${id}`, {
    method: "DELETE",
  });
  if (!res.ok) throw new Error(`Failed to delete task ${id}: ${res.status}`);
}
