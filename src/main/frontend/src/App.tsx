import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { BeerPage } from './pages/BeerPage';
import { Button } from './components/ui/button';

function App() {
  return (
    <Router>
      <div className="min-h-screen bg-background">
        <header className="border-b">
          <div className="container mx-auto py-4 px-4">
            <nav className="flex items-center justify-between">
              <div className="text-2xl font-bold">JunieMVC</div>
              <div className="flex gap-4">
                <Link to="/">
                  <Button variant="ghost">Home</Button>
                </Link>
                <Link to="/beers">
                  <Button variant="ghost">Beers</Button>
                </Link>
                <Link to="/customers">
                  <Button variant="ghost">Customers</Button>
                </Link>
                <Link to="/orders">
                  <Button variant="ghost">Orders</Button>
                </Link>
              </div>
            </nav>
          </div>
        </header>
        
        <main>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/beers/*" element={<BeerPage />} />
            <Route path="/customers" element={<ComingSoon title="Customers" />} />
            <Route path="/orders" element={<ComingSoon title="Orders" />} />
          </Routes>
        </main>
        
        <footer className="border-t mt-12">
          <div className="container mx-auto py-4 px-4 text-center text-muted-foreground">
            &copy; {new Date().getFullYear()} JunieMVC - Spring Boot + React Demo
          </div>
        </footer>
      </div>
    </Router>
  );
}

function Home() {
  return (
    <div className="container mx-auto py-8 px-4">
      <h1 className="text-4xl font-bold mb-6">Welcome to JunieMVC</h1>
      <p className="text-xl mb-8">
        A Spring Boot application with React frontend for managing beers, customers, and orders.
      </p>
      
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="border rounded-lg p-6 shadow-sm">
          <h2 className="text-2xl font-semibold mb-4">Beers</h2>
          <p className="mb-4">Manage your beer inventory, add new beers, and update existing ones.</p>
          <Link to="/beers">
            <Button>Manage Beers</Button>
          </Link>
        </div>
        
        <div className="border rounded-lg p-6 shadow-sm">
          <h2 className="text-2xl font-semibold mb-4">Customers</h2>
          <p className="mb-4">View and manage customer information and purchase history.</p>
          <Link to="/customers">
            <Button>Manage Customers</Button>
          </Link>
        </div>
        
        <div className="border rounded-lg p-6 shadow-sm">
          <h2 className="text-2xl font-semibold mb-4">Orders</h2>
          <p className="mb-4">Track beer orders, update status, and manage shipments.</p>
          <Link to="/orders">
            <Button>Manage Orders</Button>
          </Link>
        </div>
      </div>
    </div>
  );
}

function ComingSoon({ title }: { title: string }) {
  return (
    <div className="container mx-auto py-8 px-4 text-center">
      <h1 className="text-3xl font-bold mb-4">{title} Management</h1>
      <p className="text-xl mb-8">This feature is coming soon!</p>
      <Link to="/">
        <Button>Back to Home</Button>
      </Link>
    </div>
  );
}

export default App;
