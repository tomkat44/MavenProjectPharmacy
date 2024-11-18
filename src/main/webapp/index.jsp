<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Bootstrap demo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
  </head>
  <body>
  <script src="https://unpkg.com/vue@3/dist/vue.global.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>

    <div class="container pt-4">
        <h1>Welcome to pharmacyApp <br> </h1>
    </div>

  <div class="container-fluid pt-4">
  <nav class="navbar navbar-expand-md bg-dark navbar-dark">
    <div class="container-fluid pt-4">
      <div class="collapse navbar-collapse">

      <a class="navbar-brand" href="#">Logo</a>

    <!-- Links -->
    <ul class="navbar-nav">
      <li class="nav-item">
        <a class="nav-link" href="#">Link 1</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="#">Link 2</a>
      </li>

      <!-- Dropdown -->
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle" href="#" id="navbardrop" data-toggle="dropdown">
          Dropdown link
        </a>
        <div class="dropdown-menu">
          <a class="dropdown-item" href="#">Link 1</a>
          <a class="dropdown-item" href="#">Link 2</a>
          <a class="dropdown-item" href="#">Link 3</a>
        </div>
      </li>
    </ul>
</div>
      <form class="d-flex input-group w-auto">
        <input
                type="search"
                class="form-control rounded"
                placeholder="Search"
                aria-label="Search"
                aria-describedby="search-addon"
        />
        <span >
          <button class="btn btn-success" type="submit">Search</button>
      </span>

      </form>
    </div>
  </nav>
    </div>



  <div class="row">
    <div class="col"><br></div>
  </div>




  <div class="row">
    <div class="col-sm-4"></div>
    <div class="col-sm-4">
      <form action="/action_page.php" class="was-validated">
        <div class="form-group">
          <label for="uname">Username:</label>
          <input type="text" class="form-control" id="uname" placeholder="Enter username" name="uname" required>
        </div>


        <div class="form-group">
          <label for="pwd">Password:</label>
          <input type="password" class="form-control" id="pwd" placeholder="Enter password" name="pswd" required>


        </div>
        <div class="form-group form-check">
          <label class="form-check-label">
            <input class="form-check-input" type="checkbox" name="remember" required> I agree on blabla.


          </label>
        </div>
        <button type="submit" class="btn btn-primary">Submit</button>
      </form>

    </div>
    <div class="col-sm-4"></div>


  </div>

  <div>
    <button type="getDrug" class="btn btn-primary" action="/forms/getDrug.jsp">getDrug</button>
  </div>


  <script>
  const app = Vue.createApp({
   data() {
    return {
     message: "Hello World!"
    }
   }
  })

  app.mount('#app')

</script>

  </body>
</html>
