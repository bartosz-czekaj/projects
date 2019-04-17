using System;
using System.Linq;
using System.Drawing;
using System.Collections.Generic;
using System.Diagnostics;

namespace functional
{
    class Program
    {

       
        static void Main(string[] args)
        {
            var points = new List<Point>
            {
            new Point(10,20),
            new Point(100,200),
            new Point(150,250),
            new Point(250,375),
            new Point(275,395),
            new Point(295,450)
            };

            Func<Point, bool> fn = FindPoints;

            points.Where(fn.Negate().Negate()).ToList().ForEach(x=>Console.WriteLine($"Found negate, X:{x.X} Y:{x.Y}"));
            points.Where(fn).ToList().ForEach(x=>Console.WriteLine($"Found, X:{x.X} Y:{x.Y}"));

            var list = new List<int> {-100,5,6,121,3434,56,123,-212,-321,32, 63, 30, 45, 10, 1000, -23, -67, 1,2,1, 2, 56, 75, 975, 432, -600, 193, 85, 12};
            var list2 = new List<int> {-100,5,6,121,3434,56,123,-212,-321,32, 63, 30, 45, 10, 1000, -23, -67, 1,2,1, 2, 56, 75, 975, 432, -600, 193, 85, 12};
            Stopwatch sw = new Stopwatch();   

             sw.Start();
            list2.Sort();
            sw.Stop();
            Console.WriteLine(String.Join("," , list));
            Console.WriteLine("S: "+sw.ElapsedMilliseconds);
            
            testSortWithComparison(list, Comparer<int>.Default);

            testSort(list);
            
           
        }

        private static void testSort<T>(List<T> list) where T : IComparable
        {
            Stopwatch sw = new Stopwatch();   
            sw.Start();
            var lst = list.QuicSort();
            sw.Stop();
            Console.WriteLine(String.Join("," , lst));
            Console.WriteLine("TS: "+sw.ElapsedMilliseconds);
            
            
        }

        private static void testSortWithComparison<T>(List<T> list, Comparer<T> compare) 
        {
            Stopwatch sw = new Stopwatch();   
            sw.Start();
            var lst = list.QuicSortWithComparator(compare);
            sw.Stop();
            Console.WriteLine(String.Join("," , lst));
            Console.WriteLine("TSWQ: "+sw.ElapsedMilliseconds);
        }

        private static bool FindPoints(Point obj)
        {
            return obj.X * obj.Y > 100000;
        }
        
    }
}
