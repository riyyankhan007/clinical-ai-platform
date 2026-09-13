import { useEffect, useState } from "react";
import "./App.css";

type ClinicalRecord = {
  id: number;
  recordType: string;
  recordDate: string;
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
  const [patient, setPatient] = useState<Patient | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const token = localStorage.getItem("access_token");

    fetch("http://localhost:8081/api/patients/6", {
      headers: {
        Authorization: `Bearer ${token}`,
      },
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("Failed to load patient");
        }
        return response.json();
      })
      .then((data) => setPatient(data))
      .catch(() => setError("Unable to load patient information"))
      .finally(() => setLoading(false));
  }, []);

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
            <div>
              <h2>Clinical Records</h2>
              <p>{patient.records.length} records found</p>
            </div>
          </div>

          <div className="records">
            {patient.records.map((record) => (
              <article className="record-card" key={record.id}>
                <div className="record-header">
                  <span className={`record-type ${record.recordType.toLowerCase()}`}>
                    {record.recordType}
                  </span>

                  <span className="record-date">
                    {new Date(record.recordDate).toLocaleString()}
                  </span>
                </div>

                <h3>{record.title}</h3>
                <p>{record.content}</p>
              </article>
            ))}
          </div>
        </section>
      </main>
    </div>
  );
}

export default App;
