using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata.Builders;
using MonitoringService.Models;
using System;

namespace MonitoringService.Contexts
{
    public class MonitoringContext : DbContext
    {
        public DbSet<Monitor> Monitors { get; set; }
        public DbSet<MonitorParam> MonitorParams { get; set; }

        public MonitoringContext(DbContextOptions options) : base(options)
        {
            Database.EnsureCreated();
        }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (optionsBuilder is null)
            {
                throw new ArgumentNullException(nameof(optionsBuilder));
            }

            optionsBuilder.EnableSensitiveDataLogging();
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            if (modelBuilder is null)
            {
                throw new ArgumentNullException(nameof(modelBuilder));
            }

            modelBuilder.ApplyConfiguration(new MonitorsConfiguration());
            modelBuilder.ApplyConfiguration(new MonitorParamsConfiguration());
        }
    }

    public class MonitorsConfiguration : IEntityTypeConfiguration<Monitor>
    {
        public void Configure(EntityTypeBuilder<Monitor> builder)
        {
            if (builder is null)
            {
                throw new ArgumentNullException(nameof(builder));
            }

            builder.ToTable("Monitors");

            builder.HasAlternateKey(em => em.MonitorUid);

            builder.Property(em => em.Name).IsRequired();
            builder.Property(em => em.EquipmentUid).IsRequired();
            builder.Property(em => em.EquipmentModelUid).IsRequired();
        }
    }

    public class MonitorParamsConfiguration : IEntityTypeConfiguration<MonitorParam>
    {
        public void Configure(EntityTypeBuilder<MonitorParam> builder)
        {
            if (builder is null)
            {
                throw new ArgumentNullException(nameof(builder));
            }

            builder.ToTable("MonitorParams");

            builder.HasKey(mp => new { mp.MonitorId, mp.ParamUid });

            builder
                .HasOne(mp => mp.Monitor)
                .WithMany(m => m.MonitorParams)
                .HasForeignKey(mp => mp.MonitorId)
                .OnDelete(DeleteBehavior.Cascade);
        }
    }
}
