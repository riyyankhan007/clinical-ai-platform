import { useState } from 'react';
import type { FormEvent } from 'react';
import "./App.css";

type ClinicalRecord = {
  id: number;
  recordType: string;
  recordDate: string;
  sourceSystem: string;
  sourceRecordId: string;
  title: string;
  content: string;
};

type Patient = {
  id: number;
  patientIdentifier: string;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  gender: string;
  records: ClinicalRecord[];
};

function App() {
  const [authenticated, setAuthenticated] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [loginError, setLoginError] = useState("");
  const [patient, setPatient] = useState<Patient | null>(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const loadPatient = async () => {
    setLoading(true);
    setError("");

    try {
      const response = await fetch("http://localhost:8081/api/patients/6",
        {
          credentials: "include",
        }
      );

      if (response.status === 401) {
        setAuthenticated(false);
        return;
      }

      if (!response.ok) {
        throw new Error("Failed to load patient");
      }

      const data = await response.json();
      setPatient(data);
    } catch {
      setError("Unable to load patient information");
    } finally {
      setLoading(false);
    }
  };

  const handleLogin = async (event: FormEvent) => {
    event.preventDefault();
    setLoginError("");

    try {
      const response = await fetch("http://localhost:8081/api/auth/login",
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          credentials: "include",
          body: JSON.stringify({
            username,
            password,
          }),
        }
      );

      if (!response.ok) {
        setLoginError("Invalid username or password");
        return;
      }

      setAuthenticated(true);
      setPassword("");
      await loadPatient();
    } catch {
      setLoginError("Unable to connect to the clinical API");
    }
  };

  const handleLogout = async () => {
    await fetch("http://localhost:8081/api/auth/logout", {
      method: "POST",
      credentials: "include",
    });

    setAuthenticated(false);
    setPatient(null);
  };

  if (!authenticated) {
    return (
      <div className="app">
        <div className="login-card">
          <h1>Clinical Intelligence</h1>
          <p>Doctor Login</p>

          <form onSubmit={handleLogin}>
            <label>Username</label>
            <input
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="Username"
              required
            />

            <label>Password</label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Password"
              required
            />

            {loginError && (
              <p className="error">{loginError}</p>
            )}

            <button type="submit">Login</button>
          </form>
        </div>
      </div>
    );
  }

  if (loading) {
    return <div className="app">Loading patient...</div>;
  }

  if (error) {
    return <div className="app error">{error}</div>;
  }

  if (!patient) {
    return <div className="app error">Patient not found</div>;
  }

  return (
    <div className="app">
      <header className="topbar">
        <div>
          <h1>Clinical Intelligence</h1>
          <p>Patient Dashboard</p>
        </div>

        <div className="doctor">
          <span className="doctor-avatar">D</span>
          <span>Doctor</span>
          <button onClick={handleLogout}>Logout</button>
        </div>
      </header>

      <main className="dashboard">
        <section className="patient-card">
          <div className="patient-heading">
            <div className="patient-avatar">
              {patient.firstName[0]}
              {patient.lastName[0]}
            </div>

            <div>
              <h2>
                {patient.firstName} {patient.lastName}
              </h2>
              <p>{patient.patientIdentifier}</p>
            </div>
          </div>

          <div className="patient-details">
            <div>
              <span>Date of Birth</span>
              <strong>{patient.dateOfBirth}</strong>
            </div>

            <div>
              <span>Gender</span>
              <strong>{patient.gender}</strong>
            </div>

            <div>
              <span>Patient ID</span>
              <strong>{patient.id}</strong>
            </div>
          </div>
        </section>

        <section className="records-section">
          <div className="section-header">
            <h2>Clinical Timeline</h2>
            <p>{patient.records.length} records found</p>
          </div>

          <div className="timeline">
            {patient.records.map((record) => (
              <article className="timeline-item" key={record.id}>
                <div className="timeline-marker">
                  <span />
                </div>

                <div className="timeline-content">
                  <div className="timeline-date">
                    {new Date(record.recordDate).toLocaleString()}
                  </div>

                  <div className="record-card">
                    <div className="record-header">
                      <span
                        className={`record-type ${record.recordType.toLowerCase()}`}
                      >
                        {record.recordType}
                      </span>
                    </div>

                    <h3>{record.title}</h3>
                    <p>{record.content}</p>

                    <div className="record-source">
                      <span>{record.sourceSystem}</span>
                      <code>{record.sourceRecordId}</code>
                    </div>
                  </div>
                </div>
              </article>
            ))}
          </div>
        </section>
      </main>
    </div>
  );
}

export default App;




