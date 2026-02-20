"use client";

import { useEffect, useState } from "react";
import { getTasks, type Task } from "@/lib/api";

export default function TaskList() {
  const [tasks, setTasks] = useState<Task[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getTasks()
      .then(setTasks)
      .catch((err) => setError(err.message))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <p className="text-gray-500">Loading tasks...</p>;
  if (error) return <p className="text-red-500">Error: {error}</p>;

  return (
    <div className="space-y-4">
      {tasks.map((task) => (
        <div key={task.id} className="border rounded-lg p-4 hover:bg-gray-50">
          <div className="flex justify-between items-start">
            <h3 className="font-semibold text-lg">{task.title}</h3>
            <span
              className={`px-2 py-1 rounded text-sm ${
                task.status === "DONE"
                  ? "bg-green-100 text-green-800"
                  : task.status === "IN_PROGRESS"
                    ? "bg-blue-100 text-blue-800"
                    : "bg-gray-100 text-gray-800"
              }`}
            >
              {task.status.replace("_", " ")}
            </span>
          </div>
          {task.description && (
            <p className="text-gray-600 mt-2">{task.description}</p>
          )}
          <div className="mt-2 text-sm text-gray-400">
            Priority: {task.priority}
          </div>
        </div>
      ))}
    </div>
  );
}
