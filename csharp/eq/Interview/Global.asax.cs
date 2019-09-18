using System.Web.Http;
using System.Web.Mvc;
using System.Web.Optimization;
using System.Web.Routing;
using Autofac.Integration.Web;
using Autofac;
using Interview.Repository;
using System.Reflection;
using Autofac.Integration.WebApi;
using Interview.Serializer;

namespace Interview
{
    public class AutofacConfig
    {
        static readonly string _path = @"C:\Projects\C#\testEQ\data.json";

        public static void RegisterAutofac()
        {
            var builder = new ContainerBuilder();

            var config = GlobalConfiguration.Configuration;
            builder.RegisterApiControllers(Assembly.GetExecutingAssembly());
            builder.RegisterType<DataRepository>().As<IDataRepository>();
            builder.RegisterType<SerializerImp>().As<ISerializer>().SingleInstance().WithParameter(new TypedParameter(typeof(string), _path));
            

            var container = builder.Build();
            config.DependencyResolver = new AutofacWebApiDependencyResolver(container);
        }
    }

    public class WebApiApplication : System.Web.HttpApplication, IContainerProviderAccessor
    {
        static IContainerProvider _containerProvider;

        public IContainerProvider ContainerProvider
        {
            get { return _containerProvider; }
        }

        protected void Application_Start()
        {
            var builder = new ContainerBuilder();
           
            _containerProvider = new ContainerProvider(builder.Build());

            AreaRegistration.RegisterAllAreas();
            GlobalConfiguration.Configure(WebApiConfig.Register);
            FilterConfig.RegisterGlobalFilters(GlobalFilters.Filters);
            RouteConfig.RegisterRoutes(RouteTable.Routes);
            BundleConfig.RegisterBundles(BundleTable.Bundles);
            GlobalConfiguration.Configuration.Formatters.XmlFormatter.SupportedMediaTypes.Clear();

            AutofacConfig.RegisterAutofac();
        }
    }
}
