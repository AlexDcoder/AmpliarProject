import { Appointment } from "@/models/appointment"
import { Patient } from "@/models/patient"
import { Payment } from "@/models/payment"
// Corrija as importações para usar as instâncias (lowercase)
import { appointmentController } from "./appointment-controller"
import { patientController } from "./patient-controller"
import { financeController } from "./finance-controller"

export interface DashboardStats {
  monthlyRevenue: number
  pendingRevenue: number
  activePatients: number
  todayAppointmentsCount: number
}

export interface DashboardData {
  stats: DashboardStats
  upcomingAppointments: Appointment[]
}

class DashboardController {
  async getDashboardData(): Promise<DashboardData> {
    const [appointments, payments, patients] = await Promise.all([
      appointmentController.getAppointments(),
      financeController.getPayments(),
      patientController.getPatients(),
    ])

    const now = new Date()
    const today = now.toISOString().split("T")[0]
    const firstDayOfMonth = new Date(now.getFullYear(), now.getMonth(), 1)

    // Stats
    const monthlyRevenue = payments
      // Adicione a tipagem (p: Payment)
      .filter((p: Payment) => p.status === "paid" && new Date(p.date) >= firstDayOfMonth)
      // Adicione a tipagem (sum: number, p: Payment)
      .reduce((sum: number, p: Payment) => sum + p.amount, 0)

    const pendingRevenue = payments
      // Adicione a tipagem (p: Payment)
      .filter((p: Payment) => p.status === "pending")
      // Adicione a tipagem (sum: number, p: Payment)
      .reduce((sum: number, p: Payment) => sum + p.amount, 0)

    // Adicione a tipagem (p: Patient)
    const activePatients = patients.filter((p: Patient) => p.status === "active").length

    // Adicione a tipagem (a: Appointment)
    const todayAppointmentsCount = appointments.filter((a: Appointment) => a.date === today).length

    // Upcoming
    const upcomingAppointments = appointments
      // Adicione a tipagem (a: Appointment)
      .filter((a: Appointment) => new Date(a.date) >= now && a.status === "scheduled")
      // Adicione a tipagem (a: Appointment, b: Appointment)
      .sort((a: Appointment, b: Appointment) => a.appointmentDate.localeCompare(b.appointmentDate))
      .slice(0, 5)

    return {
      stats: {
        monthlyRevenue,
        pendingRevenue,
        activePatients,
        todayAppointmentsCount,
      },
      upcomingAppointments,
    }
  }
}

export const dashboardController = new DashboardController()
