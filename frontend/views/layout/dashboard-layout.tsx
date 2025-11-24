"use client"

import { useState, useEffect } from "react"
import { Sidebar } from "./sidebar"
import { Header } from "./header"
import { Dashboard } from "@/views/pages/dashboard"
import { Patients } from "@/views/pages/patients"
import { Appointments } from "@/views/pages/appointments"
import { Finance } from "@/views/pages/finance"
import { Profile } from "@/views/pages/profile"
import { Settings } from "@/views/pages/settings"
import { AuthController } from "@/controllers/auth-controller"
import type { User } from "@/models/auth"

export type Page = "dashboard" | "patients" | "appointments" | "finance" | "profile" | "settings"

export function DashboardLayout() {
  const [currentPage, setCurrentPage] = useState<Page>("dashboard")
  const [sidebarOpen, setSidebarOpen] = useState(true)
  const [user, setUser] = useState<User | null>(null)

  useEffect(() => {
    // Busca os dados do usuário que acabamos de logar
    const authController = AuthController.getInstance()
    const authState = authController.getAuthState()
    if (authState.isAuthenticated && authState.user) {
      setUser(authState.user)
    }
  }, [])

  const renderPage = () => {
    // CORREÇÃO: Passando a função `onPageChange` para os componentes filhos
    switch (currentPage) {
      case "dashboard":
        return <Dashboard onPageChange={setCurrentPage} />
      case "patients":
        return <Patients onPageChange={setCurrentPage} />
      case "appointments":
        return <Appointments onPageChange={setCurrentPage} />
      case "finance":
        return <Finance onPageChange={setCurrentPage} />
      case "profile":
        return <Profile onPageChange={setCurrentPage} />
      case "settings":
        return <Settings onPageChange={setCurrentPage} />
      default:
        return <Dashboard onPageChange={setCurrentPage} />
    }
  }

  return (
    <div className="flex h-screen bg-gray-50 dark:bg-gray-900">
      <Sidebar
        currentPage={currentPage}
        onPageChange={setCurrentPage}
        isOpen={sidebarOpen}
        onToggle={() => setSidebarOpen(!sidebarOpen)}
        user={user}
      />

      <div className="flex-1 flex flex-col overflow-hidden">
        <Header
          onMenuClick={() => setSidebarOpen(!sidebarOpen)}
          sidebarOpen={sidebarOpen}
          user={user}
        />

        <main className="flex-1 overflow-auto p-6">{renderPage()}</main>
      </div>
    </div>
  )
}
