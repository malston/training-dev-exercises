import TaskList from "@/components/TaskList";

export default function Home() {
  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <main className="max-w-3xl mx-auto px-4">
        <h1 className="text-3xl font-bold mb-2">Task Tracker</h1>
        <p className="text-gray-600 mb-8">
          Manage your tasks. Start the backend first:{" "}
          <code className="bg-gray-200 px-1 rounded">
            cd backend &amp;&amp; ./mvnw spring-boot:run
          </code>
        </p>
        <TaskList />
      </main>
    </div>
  );
}
